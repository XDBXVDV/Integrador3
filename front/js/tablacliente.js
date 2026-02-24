const API_CLIENTES = "http://localhost:8080/cliente";

document.addEventListener("DOMContentLoaded", () => {
    listarClientes();
});

function listarClientes() {
    fetch(`${API_CLIENTES}/listar`)
        .then(res => {
            if (!res.ok) throw new Error("Error al listar clientes");
            return res.json();
        })
        .then(data => {
            const tbody = document.querySelector("#tablaClientes tbody");
            tbody.innerHTML = "";

            data.forEach(c => {
                const fila = document.createElement("tr");

                fila.innerHTML = `
                    <td>${c.idCliente}</td>
                    <td>${c.nombre}</td>
                    <td>${c.apellido}</td>
                    <td>${c.dni ?? ""}</td>
                    <td>${c.telefono ?? ""}</td>
                    <td>${c.estadoUsuario}</td>
                    <button onclick="verDetalle(${c.idCliente})">
                        Ver detalle
                    </button>
                `;

                tbody.appendChild(fila);
            });
        })
        .catch(err => alert(err.message));
}
function verDetalle(idCliente) {
    window.location.href = `detalle_cliente.html?id=${idCliente}`;
}