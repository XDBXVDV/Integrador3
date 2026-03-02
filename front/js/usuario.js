const API_USUARIO = "http://localhost:8080/usuario";
let usuarioIdActual = null;

function abrirModalPassword(idUsuario) {
    if (!idUsuario) {
        alert("ID de usuario no disponible");
        return;
    }

    usuarioIdActual = idUsuario;
    document.getElementById("modalPassword").style.display = "block";
}

function cerrarModalPassword() {
    document.getElementById("modalPassword").style.display = "none";
}

async function guardarPassword() {
    const passwordActual = document.getElementById("passActual").value;
    const passwordNueva = document.getElementById("passNueva").value;

    if (!passwordActual || !passwordNueva) {
        alert("Complete todos los campos");
        return;
    }

    console.log("ID enviado:", usuarioIdActual);

    const res = await fetch("http://localhost:8080/usuario/actualizar-password", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            idUsuario: Number(usuarioIdActual),
            passwordActual: passwordActual,
            passwordNueva: passwordNueva
        })
    });

    if (!res.ok) {
        alert(await res.text());
        return;
    }

    alert("Contraseña actualizada correctamente");
    cerrarModalPassword();
}