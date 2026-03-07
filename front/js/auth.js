function verificarEmpleado() {

    const usuario = localStorage.getItem("usuario");

    if (!usuario) {

        window.location.href = "acceso_denegado.html";
        return;
    }

    const user = JSON.parse(usuario);

    if (user.rol.rolName === "CLIENTE") {

        window.location.href = "acceso_denegado.html";

    }

}