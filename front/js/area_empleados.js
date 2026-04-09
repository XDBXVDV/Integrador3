document.addEventListener("DOMContentLoaded", () => {
    validarAccesoPrivado();
    mostrarInfoUsuario();
});

function validarAccesoPrivado() {
    const usuario = JSON.parse(localStorage.getItem('usuario'));

    // Si no hay usuario o el rol es CLIENTE, lo mandamos al index
    if (!usuario || usuario.rolName.toUpperCase() === "CLIENTE") {
        console.warn("Intento de acceso no autorizado detectado.");
        window.location.href = "index.html";
        return;
    }

    // Opcional: Si tienes niveles de acceso (ADMIN vs EMPLEADO), 
    // podrías ocultar tarjetas aquí usando .style.display = 'none'
}

function mostrarInfoUsuario() {
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    if (usuario) {
        const welcome = document.getElementById("welcomeMessage");
        welcome.innerHTML = `Conectado como: <strong>${usuario.username}</strong> | Rol: <span class="badge bg-dark">${usuario.rolName}</span>`;
    }
}