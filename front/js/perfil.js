const API_CLIENTE = "http://localhost:8080/cliente";
const API_EMPLEADO = "http://localhost:8080/empleado";
const API_USUARIO = "http://localhost:8080/usuario";

// 1. Declarar variables globales primero
let datosCargados = {};
let instanciaModalEditar = null;

document.addEventListener("DOMContentLoaded", async () => {
    // Inicializar el modal aquí (asegura que Bootstrap y el HTML estén listos)
    const elModal = document.getElementById('modalEditarPerfil');
    if (elModal) {
        instanciaModalEditar = new bootstrap.Modal(elModal);
    }

    const usuarioStorage = localStorage.getItem("usuario");
    if (!usuarioStorage || usuarioStorage === "undefined") {
        window.location.href = "login.html";
        return;
    }

    const sesion = JSON.parse(usuarioStorage);

    // Cargar datos en pantalla
    document.getElementById("rolTitulo").innerText = sesion.rolName.toUpperCase();
    document.getElementById("usuarioPerfil").innerText = sesion.usuario;

    // Ejecutar peticiones a la API
    await cargarDatosDeCuenta(sesion.idUsuario);

    if (sesion.rolName.toUpperCase() === "CLIENTE") {
        await cargarPerfilPersonal(API_CLIENTE, sesion.idPersona);
    } else {
        await cargarPerfilPersonal(API_EMPLEADO, sesion.idPersona);
    }
});

async function cargarDatosDeCuenta(idUsuario) {
    try {
        const res = await fetch(`${API_USUARIO}/buscar/${idUsuario}`);
        if (res.ok) {
            const u = await res.json();
            document.getElementById("emailPerfil").innerText = u.email || "";
            document.getElementById("estadoPerfil").innerText = u.estado || "Activo";
        }
    } catch (error) {
        console.error("Error cuenta:", error);
    }
}

async function cargarPerfilPersonal(urlBase, idPersona) {
    try {
        const res = await fetch(`${urlBase}/buscar/${idPersona}`); // Ajustado a tu ruta /buscar/id
        if (!res.ok) throw new Error("No se encontró el perfil");

        const p = await res.json();
        datosCargados = p; // Guardamos los datos para el modal

        document.getElementById("nombreCompleto").innerText = `${p.nombre} ${p.apellido}`;
        document.getElementById("dniPerfil").innerText = p.dni || "---";
        document.getElementById("telefonoPerfil").innerText = p.telefono || "---";
        document.getElementById("direccionPerfil").innerText = p.direccion || "---";
    } catch (error) {
        console.error("Error perfil:", error);
    }
}

// Función global para el botón onclick
window.abrirModalEditar = function() {
    if (!instanciaModalEditar) return;

    // Precargar datos
    document.getElementById("editNombre").value = datosCargados.nombre || "";
    document.getElementById("editApellido").value = datosCargados.apellido || "";
    document.getElementById("editDni").value = datosCargados.dni || "";
    document.getElementById("editTelefono").value = datosCargados.telefono || "";
    document.getElementById("editDireccion").value = datosCargados.direccion || "";
    
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    document.getElementById("editUsuario").value = sesion.usuario;
    document.getElementById("editEmail").value = document.getElementById("emailPerfil").innerText;

    instanciaModalEditar.show();
};

document.getElementById("formEditarPerfil").addEventListener("submit", async (e) => {
    e.preventDefault();
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    
    try {
        const urlBase = sesion.rolName.toUpperCase() === "CLIENTE" ? API_CLIENTE : API_EMPLEADO;
        
        // 1. PUT a Persona
        const resPersona = await fetch(`${urlBase}/actualizar/${sesion.idPersona}`, {
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

        // 2. PUT a Usuario
        const resUsuario = await fetch(`${API_USUARIO}/actualizar`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                idUsuario: sesion.idUsuario,
                usuario: document.getElementById("editUsuario").value,
                email: document.getElementById("editEmail").value
            })
        });

        if (resPersona.ok && resUsuario.ok) {
            alert("Perfil actualizado");
            sesion.usuario = document.getElementById("editUsuario").value;
            localStorage.setItem("usuario", JSON.stringify(sesion));
            location.reload();
        } else {
            alert("Error al actualizar");
        }
    } catch (error) {
        alert("Error de conexión");
    }
});