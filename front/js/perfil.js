const API_CLIENTE = "http://localhost:8080/cliente";
const API_EMPLEADO = "http://localhost:8080/empleado";
const API_USUARIO = "http://localhost:8080/usuario";
const API_VENTAS = "http://localhost:8080/producto/ventas";

let datosCargados = {};
let modalEditar = null;
let modalDetalle = null;

document.addEventListener("DOMContentLoaded", async () => {
    // Inicializar modales
    modalEditar = new bootstrap.Modal(document.getElementById('modalEditarPerfil'));
    modalDetalle = new bootstrap.Modal(document.getElementById('modalDetallePedido'));

    const sesion = JSON.parse(localStorage.getItem("usuario"));
    if (!sesion) { window.location.href = "login.html"; return; }

    // 1. Cargar UI básica
    document.getElementById("rolTitulo").innerText = sesion.rolName;
    document.getElementById("usuarioPerfil").innerText = sesion.usuario;

    // 2. Cargar Datos Extendidos
    await cargarDatosDeCuenta(sesion.idUsuario);

    const esCliente = sesion.rolName.toUpperCase() === "CLIENTE";

    if (esCliente) {
        await cargarPerfilPersonal(API_CLIENTE, sesion.idPersona);
        document.getElementById("seccionHistorial").style.display = "block";
        await cargarHistorialPedidos(sesion.idPersona);
    } else {
        await cargarPerfilPersonal(API_EMPLEADO, sesion.idPersona);
        document.getElementById("seccionHistorial").style.display = "none";
    }
});

// --- LÓGICA DE CARGA ---

async function cargarDatosDeCuenta(idUsuario) {
    try {
        const res = await fetch(`${API_USUARIO}/${idUsuario}`);
        if (res.ok) {
            const u = await res.json();
            document.getElementById("emailPerfil").innerText = u.email || "No registrado";
            document.getElementById("estadoPerfil").innerText = u.estado || "Activo";
        }
    } catch (e) { console.error(e); }
}

async function cargarPerfilPersonal(urlBase, idPersona) {
    try {
        const res = await fetch(`${urlBase}/buscar/${idPersona}`);
        if (res.ok) {
            datosCargados = await res.json();
            document.getElementById("nombreCompleto").innerText = `${datosCargados.nombre} ${datosCargados.apellido}`;
            document.getElementById("dniPerfil").innerText = datosCargados.dni || "---";
            document.getElementById("telefonoPerfil").innerText = datosCargados.telefono || "---";
            document.getElementById("direccionPerfil").innerText = datosCargados.direccion || "---";
        }
    } catch (e) { console.error(e); }
}

async function cargarHistorialPedidos(idCliente) {
    const tabla = document.getElementById("lista-pedidos");
    try {
        const res = await fetch(`${API_VENTAS}/cliente/${idCliente}`);
        const pedidos = await res.json();
        tabla.innerHTML = "";

        if (pedidos.length === 0) {
            tabla.innerHTML = '<tr><td colspan="5" class="text-center p-3">No hay pedidos registrados</td></tr>';
            return;
        }

        pedidos.forEach(p => {
            const fecha = new Date(p.fechaventa).toLocaleDateString();
            tabla.innerHTML += `
                <tr>
                    <td><strong>#${p.idVenta}</strong></td>
                    <td>${fecha}</td>
                    <td>S/ ${p.total.toFixed(2)}</td>
                    <td><span class="badge bg-primary">${p.estado}</span></td>
                    <td>
                        <button class="btn btn-sm btn-info" onclick="verDetallePedido(${p.idVenta}, ${p.total})">Ver</button>
                    </td>
                </tr>
            `;
        });
    } catch (e) { tabla.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Error al cargar historial</td></tr>'; }
}

// --- FUNCIONES GLOBALES ---

window.abrirModalEditar = () => {
    document.getElementById("editNombre").value = datosCargados.nombre || "";
    document.getElementById("editApellido").value = datosCargados.apellido || "";
    document.getElementById("editDni").value = datosCargados.dni || "";
    document.getElementById("editTelefono").value = datosCargados.telefono || "";
    document.getElementById("editDireccion").value = datosCargados.direccion || "";
    
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    document.getElementById("editUsuario").value = sesion.usuario;
    document.getElementById("editEmail").value = document.getElementById("emailPerfil").innerText;
    modalEditar.show();
};

window.verDetallePedido = async (idVenta, total) => {
    try {
        const res = await fetch(`${API_VENTAS}/detalle/${idVenta}`);
        const detalles = await res.json();
        
        const tablaDetalle = document.getElementById("lista-detalle-pedido");
        document.getElementById("numPedido").innerText = idVenta;
        document.getElementById("totalPedidoModal").innerText = total.toFixed(2);
        
        tablaDetalle.innerHTML = "";
        detalles.forEach(d => {
            tablaDetalle.innerHTML += `
                <tr>
                    <td>${d.nombreProducto}</td>
                    <td>S/ ${d.precioUnitario.toFixed(2)}</td>
                    <td>${d.cantidad}</td>
                    <td>S/ ${d.subtotal.toFixed(2)}</td>
                </tr>
            `;
        });
        modalDetalle.show();
    } catch (e) { alert("No se pudo cargar el detalle del pedido"); }
};

document.getElementById("formEditarPerfil").addEventListener("submit", async (e) => {
    e.preventDefault();
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    const urlBase = sesion.rolName.toUpperCase() === "CLIENTE" ? API_CLIENTE : API_EMPLEADO;

    try {
        await fetch(`${urlBase}/actualizar/${sesion.idPersona}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                nombre: document.getElementById("editNombre").value,
                apellido: document.getElementById("editApellido").value,
                dni: document.getElementById("editDni").value,
                telefono: document.getElementById("editTelefono").value,
                direccion: document.getElementById("editDireccion").value
            })
        });

        await fetch(`${API_USUARIO}/actualizar`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                idUsuario: sesion.idUsuario,
                usuario: document.getElementById("editUsuario").value,
                email: document.getElementById("editEmail").value
            })
        });

        alert("Datos actualizados");
        sesion.usuario = document.getElementById("editUsuario").value;
        localStorage.setItem("usuario", JSON.stringify(sesion));
        location.reload();
    } catch (e) { alert("Error al actualizar"); }
});