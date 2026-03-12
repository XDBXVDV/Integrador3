function verificarEmpleado() {
    const usuario = localStorage.getItem("usuario");
    if (!usuario || usuario === "undefined") {
        window.location.href = "acceso_denegado.html";
        return;
    }

    const user = JSON.parse(usuario);
    if (user.rolName.toUpperCase() === "CLIENTE") {
        window.location.href = "acceso_denegado.html";
    }
}

function soloadmin() {
    const usuario = localStorage.getItem("usuario");
    if (!usuario) {
        window.location.href = "acceso_denegado.html";
        return;
    }

    const user = JSON.parse(usuario);
    if (!user.rolName || user.rolName.toUpperCase() !== "ADMINISTRADOR") {
        window.location.href = "acceso_denegado.html";
    }
}