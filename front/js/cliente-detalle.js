const API_CLIENTE = "http://localhost:8080/cliente";


const params = new URLSearchParams(window.location.search);
const idCliente = params.get("id");

let idUsuarioGlobal = null;
let estadoUsuarioGlobal = null;

if (!idCliente) {
    alert("ID de cliente no encontrado");
}

// Cargar datos al abrir la página
document.addEventListener("DOMContentLoaded", () => {
    cargarCliente();
});

async function cargarCliente() {
    try {
        const response = await fetch(`${API_CLIENTE}/buscar/${idCliente}`);
        if (!response.ok) throw new Error("No se pudo cargar el cliente");

        const cliente = await response.json();

        document.getElementById("idCliente").value = cliente.idCliente;
        document.getElementById("nombre").value = cliente.nombre;
        document.getElementById("apellido").value = cliente.apellido;
        document.getElementById("dni").value = cliente.dni;
        document.getElementById("telefono").value = cliente.telefono ?? "";
        document.getElementById("direccion").value = cliente.direccion ?? "";
        document.getElementById("usuario").value = cliente.usuario;
        document.getElementById("email").value = cliente.email;

        idUsuarioGlobal = cliente.idUsuario;
        estadoUsuarioGlobal = cliente.estadoUsuario;

        // Mostrar u ocultar botón
        if (estadoUsuarioGlobal !== "Activo") {
            document.getElementById("btnDesactivar").style.display = "none";
        }

    } catch (error) {
        alert(error.message);
    }
}


document.getElementById("formEditarCliente").addEventListener("submit", async (e) => {
    e.preventDefault();

    const body = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        dni: document.getElementById("dni").value,
        telefono: document.getElementById("telefono").value,
        direccion: document.getElementById("direccion").value,
        usuario: document.getElementById("usuario").value,
        email: document.getElementById("email").value
    };

    try {
        const response = await fetch(`${API_CLIENTE}/actualizar/${idCliente}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            throw new Error("Error al actualizar los datos");
        }

        alert("Cliente actualizado correctamente");

    } catch (error) {
        alert(error.message);
    }
});

document.getElementById("btnDesactivar").addEventListener("click", async () => {
    if (!confirm("¿Seguro que deseas desactivar este cliente?")) return;

    try {
        const response = await fetch(
            `${API_USUARIO}/desactivar/${idUsuarioGlobal}`,
            { method: "PUT" }
        );

        if (!response.ok) throw new Error("Error al desactivar");

        alert("Cliente desactivado");
        location.reload();

    } catch (error) {
        alert(error.message);
    }
});