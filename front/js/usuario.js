const API_USUARIO = "http://localhost:8080/usuario";
let usuarioIdActual = null;

function abrirModalPassword(idUsuario) {
    usuarioIdActual = idUsuario;
    document.getElementById("modalPassword").style.display = "block";
}

function cerrarModalPassword() {
    document.getElementById("modalPassword").style.display = "none";
}

async function guardarPassword() {

    const contrasenaActual = document.getElementById("passActual").value;
    const nuevaContrasena = document.getElementById("passNueva").value;

    if (!contrasenaActual || !nuevaContrasena) {
        alert("Complete todos los campos");
        return;
    }

    const res = await fetch(`${API_USUARIO}/actualizar-contrasena`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            idUsuario: usuarioIdActual,
            contrasenaActual,
            nuevaContrasena
        })
    });

    if (!res.ok) {
        alert(await res.text());
        return;
    }

    alert("Contraseña actualizada correctamente");
    cerrarModalPassword();
}