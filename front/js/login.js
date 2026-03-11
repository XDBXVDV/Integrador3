const API_AUTH = "http://localhost:8080/auth";
const API_CLIENTES = "http://localhost:8080/cliente";

// --- PROCESO DE LOGIN ---
document.getElementById("formLogin").addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuario = document.getElementById("usuarioLogin").value;
    const contrasena = document.getElementById("contrasenaLogin").value;

    const body = {
        usuario: usuario,
        contrasena: contrasena
    };

    try {
        const res = await fetch(`${API_AUTH}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            // Si el backend lanza error (401 o 400), capturamos el mensaje
            const errorText = await res.text();
            throw new Error(errorText || "Error en la autenticación");
        }

        // data recibirá: { idUsuario, usuario, rolName, idPersona }
        const data = await res.json();

        // Guardamos el objeto completo para tener el idPersona disponible para el carrito
        localStorage.setItem("usuario", JSON.stringify(data));

        // Redirigir según el rol si fuera necesario, o al index por defecto
        window.location.href = "index.html";

    } catch (error) {
        alert("Fallo al iniciar sesión: " + error.message);
    }
});

// --- PROCESO DE REGISTRO ---
document.getElementById("formRegistro").addEventListener("submit", async (e) => {
    e.preventDefault();

    const body = {
        usuario: document.getElementById("usuarioRegistro").value,
        email: document.getElementById("email").value,
        contrasena: document.getElementById("contrasena").value,
        idRol: 1, // Rol Cliente por defecto
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        dni: document.getElementById("dni").value,
        telefono: document.getElementById("telefono").value,
        direccion: document.getElementById("direccion").value
    };

    try {
        const res = await fetch(`${API_CLIENTES}/crear`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            const errorMsg = await res.text();
            throw new Error(errorMsg || "No se pudo crear la cuenta");
        }

        alert("¡Cuenta creada con éxito! Ahora puedes iniciar sesión.");
        
        // Cerrar modal y resetear form
        document.getElementById("formRegistro").reset();
        const modalElement = document.getElementById("modalRegistro");
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        modalInstance.hide();

    } catch (error) {
        alert("Error en el registro: " + error.message);
    }
});