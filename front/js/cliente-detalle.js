const API_CLIENTES = "http://localhost:8080/cliente";

const params = new URLSearchParams(window.location.search);
const idCliente = params.get("id");

document.addEventListener("DOMContentLoaded", () => {
    cargarDetalle();
});

function cargarDetalle() {
    fetch(`${API_CLIENTES}/buscar/${idCliente}`)
        .then(res => {
            if (!res.ok) throw new Error("Error al cargar detalle");
            return res.json();
        })
        .then(c => {
            document.getElementById("nombre").textContent = c.nombre;
            document.getElementById("apellido").textContent = c.apellido;
            document.getElementById("dni").textContent = c.dni ?? "";
            document.getElementById("telefono").textContent = c.telefono ?? "";
            document.getElementById("direccion").textContent = c.direccion ?? "";

            document.getElementById("usuario").textContent = c.usuario;
            document.getElementById("email").textContent = c.email;
            document.getElementById("rol").textContent = c.rol;
            document.getElementById("estado").textContent = c.estadoUsuario;
        })
        .catch(err => alert(err.message));
}

function volver() {
    window.history.back();
}