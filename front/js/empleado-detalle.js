const API_EMPLEADO = "http://localhost:8080/empleado";


const params = new URLSearchParams(window.location.search);
const idEmpleado = params.get("id");

let idUsuarioGlobal = null;

if (!idEmpleado) {
    alert("ID de empleado no encontrado");
}

document.addEventListener("DOMContentLoaded", () => {
    cargarEmpleado();
});

async function cargarEmpleado() {
    try {
        const res = await fetch(`${API_EMPLEADO}/buscar/${idEmpleado}`);
        if (!res.ok) throw new Error("No se pudo cargar el empleado");

        const empleado = await res.json();

        document.getElementById("idEmpleado").value = empleado.idEmpleado;
        document.getElementById("nombre").value = empleado.nombre;
        document.getElementById("apellido").value = empleado.apellido;
        document.getElementById("dni").value = empleado.dni ?? "";

        document.getElementById("usuario").value = empleado.usuario;
        document.getElementById("email").value = empleado.email;

        idUsuarioGlobal = empleado.idUsuario;

        console.log("ID USUARIO:", idUsuarioGlobal);

    } catch (err) {
        alert(err.message);
    }
}
document.getElementById("formEditarEmpleado").addEventListener("submit", async (e) => {
    e.preventDefault();

    try {
        // 1️⃣ Empleado
        const empleadoBody = {
            nombre: document.getElementById("nombre").value,
            apellido: document.getElementById("apellido").value,
            dni: document.getElementById("dni").value
        };

        const empRes = await fetch(`${API_EMPLEADO}/actualizar/${idEmpleado}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(empleadoBody)
        });

        if (!empRes.ok) throw new Error("Error al actualizar empleado");

        // 2️⃣ Usuario
        const usuarioBody = {
            idUsuario: idUsuarioGlobal,
            usuario: document.getElementById("usuario").value,
            email: document.getElementById("email").value
        };

        const userRes = await fetch(`${API_USUARIO}/actualizar`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuarioBody)
        });

        if (!userRes.ok) throw new Error("Error al actualizar usuario");

        alert("Empleado actualizado correctamente");

    } catch (err) {
        alert(err.message);
    }
});