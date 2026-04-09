// Verifica que sea cualquier tipo de empleado o admin
function verificarEmpleado() {
    const usuarioStr = localStorage.getItem("usuario");
    if (!usuarioStr || usuarioStr === "undefined") {
        window.location.href = "acceso_denegado.html";
        return;
    }

    const user = JSON.parse(usuarioStr);
    const rol = user.rolName.toUpperCase();

    const rolesPermitidos = [
        "ADMINISTRADOR", 
        "AREA DE VENTAS", 
        "AREA DE COMPRAS", 
        "AREA DE ALMACÉN"
    ];

    if (!rolesPermitidos.includes(rol)) {
        window.location.href = "acceso_denegado.html";
    }
}

function soloadmin() {
    const usuarioStr = localStorage.getItem("usuario");
    const user = JSON.parse(usuarioStr);
    
    if (user.rolName.toUpperCase() !== "ADMINISTRADOR") {
        window.location.href = "acceso_denegado.html";
    }
}

function mostrarSeccionesPorRol() {
    const usuarioStr = localStorage.getItem("usuario");
    if (!usuarioStr) return;
    
    const user = JSON.parse(usuarioStr);
    const rol = user.rolName.toUpperCase();

    // Si es admin, mostramos la sección de seguridad y usuarios
    if (rol === "ADMINISTRADOR") {
        const secSeguridad = document.getElementById("seccionSeguridad");
        if(secSeguridad) secSeguridad.style.display = "block";
    }
}