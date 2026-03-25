const API_BASE = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {
    cargarProveedores();
    cargarRequerimientosPendientes();
});

async function cargarProveedores() {
    const res = await fetch(`http://localhost:8080/proveedor/activos`);
    const proveedores = await res.json();
    const select = document.getElementById("selectProveedor");
    proveedores.forEach(p => {
        select.innerHTML += `<option value="${p.idProveedor}">${p.razonSocial}</option>`;
    });
}

async function cargarRequerimientosPendientes() {
    const res = await fetch(`${API_BASE}/requerimientos/pendientes`);
    const requerimientos = await res.json();
    const tbody = document.getElementById("listaRequerimientos");
    tbody.innerHTML = "";

    requerimientos.forEach(r => {
        const pClass = r.prioridad === 'CRITICA' ? 'bg-danger' : 'bg-warning text-dark';
        tbody.innerHTML += `
            <tr>
                <td><input type="checkbox" class="chk-req" value="${r.idRequerimiento}"></td>
                <td>
                    <strong>${r.producto.nombre}</strong><br>
                    <small class="text-muted">Stock actual: ${r.producto.stock}</small>
                </td>
                <td class="text-center fw-bold">${r.cantidadSugerida}</td>
                <td><span class="badge ${pClass}">${r.prioridad}</span></td>
                <td>${r.empleadoAlmacen.nombre}</td>
            </tr>
        `;
    });
}

async function generarPedidoOficial() {
    const idProv = document.getElementById("selectProveedor").value;
    const seleccionados = Array.from(document.querySelectorAll(".chk-req:checked")).map(cb => Number(cb.value));
    
    const usuarioStr = localStorage.getItem("usuario");
    if (!usuarioStr) return alert("Sesión no válida.");
    const usuario = JSON.parse(usuarioStr);

    // DEPURA AQUÍ: Mira si el ID del empleado existe
    console.log("Usuario actual:", usuario);

    const data = {
        // ESTOS NOMBRES DEBEN SER IDÉNTICOS AL DTO DE JAVA
        idsRequerimientos: seleccionados, 
        idProveedor: Number(idProv),
        idEmpleadoLogistica: Number(usuario.idEmpleado || usuario.id || usuario.idUsuario)
    };

    console.log("Enviando JSON:", data);

    if (confirm("¿Desea generar el pedido?")) {
        const res = await fetch(`${API_BASE}/pedidos-compra/convertir-desde-requerimientos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert("¡Pedido generado!");
            window.location.href = "gestion_pedidos.html";
        } else {
            const txt = await res.text();
            alert(txt); // Aquí verás el error detallado
        }
    }
}

function toggleAll(source) {
    document.querySelectorAll('.chk-req').forEach(cb => cb.checked = source.checked);
}