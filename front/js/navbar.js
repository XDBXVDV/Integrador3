document.addEventListener("DOMContentLoaded", async () => {
    const container = document.getElementById("navbarContainer");
    if (!container) return;


    const res = await fetch("../components/navbarclientes.html");
    const html = await res.text();
    container.innerHTML = html;

    
    iniciarNavbar();
    actualizarContadorNavbar();
});

function getUsuarioSesion() {
    const usuario = localStorage.getItem("usuario");
    
    if (!usuario || usuario === "undefined") return null;
    try {
        return JSON.parse(usuario);
    } catch (e) {
        return null;
    }
}

function iniciarNavbar() {
    const usuario = getUsuarioSesion();
    const usuarioInfo = document.getElementById("usuarioInfo");
    const btnLogin = document.getElementById("btnLogin");
    const btnLogout = document.getElementById("btnLogout");
    const areaEmpleado = document.getElementById("areaEmpleado");
    const btnUsuario = document.getElementById("btnUsuario");
    const dropdown = document.getElementById("dropdownUsuario");

   
    if (!usuarioInfo || !btnLogin || !btnLogout) return;

    if (!usuario) {
        usuarioInfo.innerText = "Invitado";
        btnLogin.style.display = "block";
        btnLogout.style.display = "none";
        if (areaEmpleado) areaEmpleado.style.display = "none";
    } else {

        const nombreRol = usuario.rolName || "CLIENTE";
        usuarioInfo.innerText = `${usuario.usuario} (${nombreRol})`;

        btnLogin.style.display = "none";
        btnLogout.style.display = "block";

        if (areaEmpleado) {
            if (nombreRol.toUpperCase() !== "CLIENTE") {
                areaEmpleado.style.display = "inline";
            } else {
                areaEmpleado.style.display = "none";
            }
        }
    }

    if (btnUsuario && dropdown) {
        btnUsuario.onclick = (e) => {
            e.stopPropagation();
            dropdown.classList.toggle("show");
        };
    }
}

document.addEventListener("click", () => {
    const dropdown = document.getElementById("dropdownUsuario");
    if (dropdown) dropdown.classList.remove("show");
});

window.actualizarContadorNavbar = function() {
    const carrito = JSON.parse(localStorage.getItem('carrito')) || [];
    const totalItems = carrito.reduce((acc, item) => acc + item.cantidad, 0);
    const badge = document.getElementById('carrito-count');
    if (badge) badge.innerText = totalItems;
};

function logout() {
    localStorage.removeItem("usuario");
    localStorage.removeItem("carrito");
    window.location.href = "login.html";
}