const API_CLIENTES = "http://localhost:8080/cliente";
const API_USUARIO = "http://localhost:8080/usuario";
document.addEventListener("DOMContentLoaded", () => {
    listarClientes();
});

async function listarClientes() {
    try {
        const response = await fetch(`${API_CLIENTES}/listar`);
        if (!response.ok) throw new Error("Error al listar clientes");

        const clientes = await response.json();
        const tbody = document.getElementById("tablaClientes");
        tbody.innerHTML = "";

        clientes.forEach(c => {

            let botonesAccion = `
                <button onclick="verDetalle(${c.idCliente})">
                    Ver detalles
                </button>
            `;

            if (c.estadoUsuario === "Activo") {
                botonesAccion += `
                    <button 
                        onclick="desactivarUsuario(${c.idUsuario})"
                        style="background:red;color:white;margin-left:5px">
                        Desactivar
                    </button>
                `;
            } else {
                botonesAccion += `
                    <button 
                        onclick="activarUsuario(${c.idUsuario})"
                        style="background:green;color:white;margin-left:5px">
                        Activar
                    </button>
                `;
            }

            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${c.nombre} ${c.apellido}</td>
                <td>${c.dni}</td>
                <td>${c.telefono ?? ""}</td>
                <td>${c.estadoUsuario}</td>
                <td>${botonesAccion}</td>
            `;

            tbody.appendChild(fila);
        });

    } catch (error) {
        alert(error.message);
    }
}
function verDetalle(idCliente) {
    window.location.href = `detalle_cliente.html?id=${idCliente}`;
}

async function desactivarUsuario(idUsuario) {
    if (!confirm("¿Desactivar cuenta de cliente?")) return;

    await fetch(`${API_USUARIO}/desactivar/${idUsuario}`, { method: "PUT" });
    listarClientes();
}

async function activarUsuario(idUsuario) {
    if (!confirm("¿Activar cliente?")) return;

    await fetch(`${API_USUARIO}/reactivar/${idUsuario}`, { method: "PUT" });
    listarClientes();
}

