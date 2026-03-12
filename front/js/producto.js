const API_URL = "http://localhost:8080/producto";
let modalProducto;
let form;
let tablaData;

// Inicialización segura
document.addEventListener("DOMContentLoaded", () => {
    // Inicializamos variables de DOM aquí para asegurar que existan
    const modalElement = document.getElementById('modalProducto');
    if (modalElement) modalProducto = new bootstrap.Modal(modalElement);
    form = document.getElementById('formProducto');

    // Ejecutamos cargas iniciales
    listarProductos();
    cargarCombos();

    // Configurar el evento submit del formulario
    if (form) {
        form.onsubmit = guardarProducto;
    }
});

/* =========================
   FUNCIONES GLOBALES (window)
========================= */

window.listarProductos = async function() {
    try {
        const resp = await fetch(`${API_URL}/listar`);
        const productos = await resp.json();
        const tabla = document.getElementById("tablaProductos");

        if (tablaData) tablaData.destroy();
        tabla.innerHTML = "";

        productos.forEach(p => {
            const botonEstado = p.estado === "Activo" 
                ? `<button class="btn btn-outline-danger btn-sm" onclick="desactivarProducto(${p.idProducto})">Desactivar</button>`
                : `<button class="btn btn-outline-success btn-sm" onclick="reactivarProducto(${p.idProducto})">Reactivar</button>`;

            let badgeCondicion = p.condicion === "Agotado" ? "bg-danger" : (p.condicion === "Stock_bajo" ? "bg-warning text-dark" : "bg-success");

            tabla.innerHTML += `
                <tr>
                    <td>${p.idProducto}</td>
                    <td><img src="${p.imagen ? 'http://localhost:8080'+p.imagen : 'https://via.placeholder.com/60'}" width="50" class="rounded"></td>
                    <td>${p.nombre}</td>
                    <td>${p.categoria ? p.categoria.nombre : ""}</td>
                    <td>${p.marca ? p.marca.nombre : ""}</td>
                    <td>S/ ${p.precio.toFixed(2)}</td>
                    <td>${p.stock}</td>
                    <td>${p.stockMinimo}</td>
                    <td><span class="badge ${p.estado === 'Activo' ? 'bg-info' : 'bg-secondary'}">${p.estado}</span></td>
                    <td><span class="badge ${badgeCondicion}">${p.condicion.replace('_',' ')}</span></td>
                    <td>
                        <div class="d-flex gap-1">
                            ${botonEstado}
                            <button class="btn btn-warning btn-sm" onclick="prepararEdicion(${p.idProducto})">Editar</button>
                        </div>
                    </td>
                </tr>`;
        });

        tablaData = new DataTable('#tablaProductosData', {
            pageLength: 10,
            language: { url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json' }
        });
    } catch (e) { console.error("Error al listar:", e); }
};

window.abrirModalCrear = function() {
    form.reset();
    document.getElementById("idProducto").value = "";
    document.getElementById("imagenPreview").style.display = "none";
    document.getElementById("modalTitulo").innerText = "Nuevo Producto";
    modalProducto.show();
};

window.cargarCombos = async function() {
    try {
        const [resCat, resMarca] = await Promise.all([
            fetch(`http://localhost:8080/categoria/listar/activos`),
            fetch(`http://localhost:8080/marca/listar/activos`)
        ]);

        const categorias = await resCat.json();
        const marcas = await resMarca.json();

        const selectCat = document.getElementById("idCategoria");
        const selectMarca = document.getElementById("idMarca");

        selectCat.innerHTML = '<option value="">Seleccione Categoría...</option>';
        categorias.forEach(c => selectCat.innerHTML += `<option value="${c.idCategoria}">${c.nombre}</option>`);

        selectMarca.innerHTML = '<option value="">Seleccione Marca...</option>';
        marcas.forEach(m => selectMarca.innerHTML += `<option value="${m.idMarca}">${m.nombre}</option>`);
    } catch (e) { console.error("Error combos:", e); }
};

async function guardarProducto(e) {
    e.preventDefault();
    const id = document.getElementById("idProducto").value;
    const formData = new FormData(form); 
    
    // Si usas FormData(form), asegúrate que los 'name' de los inputs coincidan con los @RequestParam del Controller
    // Si no, añádelos manualmente como antes:
    const manualData = new FormData();
    manualData.append("nombre", document.getElementById("nombre").value);
    manualData.append("descripcion", document.getElementById("descripcion").value);
    manualData.append("precio", document.getElementById("precio").value);
    manualData.append("stock", document.getElementById("stock").value);
    manualData.append("stockMinimo", document.getElementById("stockMinimo").value);
    manualData.append("estado", document.getElementById("estado").value);
    manualData.append("categoriaId", document.getElementById("idCategoria").value);
    manualData.append("marcaId", document.getElementById("idMarca").value);
    
    const file = document.getElementById("imagen").files[0];
    if(file) manualData.append("imagen", file);

    const url = id ? `${API_URL}/actualizar/${id}` : `${API_URL}/crear`;
    const res = await fetch(url, { method: id ? 'PUT' : 'POST', body: manualData });

    if (res.ok) {
        alert("Guardado con éxito");
        modalProducto.hide();
        listarProductos();
    }
}

// Exponer las funciones que faltan para los botones de la tabla
window.prepararEdicion = async function(id) {
    const res = await fetch(`${API_URL}/buscar/${id}`);
    const p = await res.json();
    document.getElementById("idProducto").value = p.idProducto;
    document.getElementById("nombre").value = p.nombre;
    document.getElementById("idCategoria").value = p.categoria.idCategoria;
    document.getElementById("idMarca").value = p.marca.idMarca;
    // ... llenar el resto de campos ...
    modalProducto.show();
};

window.desactivarProducto = async (id) => {
    if(confirm("¿Desactivar?")) {
        await fetch(`${API_URL}/desactivar/${id}`, { method: 'PUT' });
        listarProductos();
    }
};

window.reactivarProducto = async (id) => {
    if(confirm("¿Reactivar?")) {
        await fetch(`${API_URL}/activar/${id}`, { method: 'PUT' });
        listarProductos();
    }
};