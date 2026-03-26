const API_URL = "http://localhost:8080/api";
let productosMover = []; 
let modalDetalles; 

document.addEventListener("DOMContentLoaded", () => {
    listarGuias();
    cargarComboProductos();
    // Inicializamos el modal de detalles
    modalDetalles = new bootstrap.Modal(document.getElementById('modalDetalles'));
});

async function listarGuias() {
    const res = await fetch(`${API_URL}/guias/listar`);
    const guias = await res.json();
    const tbody = document.getElementById("tablaGuias");
    tbody.innerHTML = "";

    guias.forEach(g => {
        const badge = g.tipoMovimiento === 'ENTRADA' ? 'bg-success' : 'bg-danger';
        tbody.innerHTML += `
            <tr>
                <td><strong>${g.numeroGuia}</strong></td>
                <td><span class="badge ${badge}">${g.tipoMovimiento}</span></td>
                <td>${new Date(g.fechaMovimiento).toLocaleString()}</td>
                <td>${g.motivo}</td>
                <td>${g.empleadoAlmacen.nombre}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary" onclick="verDetalles(${g.idGuia})">
                        <i class="fas fa-eye">Ver detalles</i>
                    </button>
                </td>
            </tr>`;
    });
}

// --- FUNCIÓN PARA VER DETALLES ---
async function verDetalles(idGuia) {
    try {
        const res = await fetch(`${API_URL}/guias/detalles/${idGuia}`);
        const detalles = await res.json();
        
        const tbody = document.getElementById("listaDetalles");
        tbody.innerHTML = "";

        detalles.forEach(d => {
            tbody.innerHTML += `
                <tr>
                    <td>${d.producto.nombre}</td>
                    <td class="text-center fw-bold">${d.cantidad}</td>
                </tr>`;
        });

        // Configurar botón PDF
        document.getElementById("btnDescargarPdf").onclick = () => {
            window.open(`${API_URL}/guias/pdf/${idGuia}`, '_blank');
        };

        modalDetalles.show();
    } catch (error) {
        console.error("Error al cargar detalles:", error);
        alert("No se pudieron cargar los detalles.");
    }
}

async function cargarComboProductos() {
    const res = await fetch(`http://localhost:8080/producto/listarDTO`);
    const productos = await res.json();
    const select = document.getElementById("selectProd");
    productos.forEach(p => {
        select.innerHTML += `<option value="${p.idProducto}">${p.nombre} (Stock: ${p.stock})</option>`;
    });
}

function agregarFila() {
    const select = document.getElementById("selectProd");
    const id = select.value;
    const nombre = select.options[select.selectedIndex].text;
    const cant = parseInt(document.getElementById("cantProd").value);

    if (cant <= 0) return alert("Ingrese una cantidad válida");

    productosMover.push({ idProducto: id, nombre: nombre, cantidad: cant });
    renderizarTablaTemporal();
}

function renderizarTablaTemporal() {
    const tbody = document.getElementById("listaTemporal");
    tbody.innerHTML = "";
    productosMover.forEach((item, index) => {
        tbody.innerHTML += `
            <tr>
                <td>${item.nombre}</td>
                <td>${item.cantidad}</td>
                <td><button class="btn btn-sm btn-danger" onclick="eliminarFila(${index})">x</button></td>
            </tr>`;
    });
}

function eliminarFila(index) {
    productosMover.splice(index, 1);
    renderizarTablaTemporal();
}

async function guardarGuiaCompleta() {
    if (productosMover.length === 0) return alert("Debe agregar al menos un producto.");

    const usuarioStr = localStorage.getItem("usuario");
    if (!usuarioStr) return alert("Sesión no válida.");
    const usuario = JSON.parse(usuarioStr);
    
    const idEmpleado = Number(usuario.idEmpleado || usuario.id || usuario.idUsuario);

    const guiaDTO = {
        numeroGuia: document.getElementById("numGuia").value,
        tipoMovimiento: document.getElementById("tipoMov").value,
        motivo: document.getElementById("motivoGuia").value,
        idEmpleadoAlmacen: idEmpleado,
        items: productosMover.map(p => ({ 
            idProducto: Number(p.idProducto), 
            cantidad: Number(p.cantidad) 
        }))
    };

    if (isNaN(guiaDTO.idEmpleadoAlmacen)) return alert("Error: ID de empleado no válido.");

    try {
        const res = await fetch(`${API_URL}/guias/registrar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(guiaDTO)
        });

        if (res.ok) {
            alert("Guía registrada exitosamente.");
            location.reload();
        } else {
            const error = await res.text();
            alert("Error: " + error);
        }
    } catch (err) {
        console.error("Error en fetch:", err);
    }
}