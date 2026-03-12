const API_CLIENTE = "http://localhost:8080/cliente";
const API_EMPLEADO = "http://localhost:8080/empleado";
const API_USUARIO = "http://localhost:8080/usuario";

let datosCargados = {};
let modalEditar, modalPassword;

document.addEventListener("DOMContentLoaded", async () => {
    modalEditar = new bootstrap.Modal(document.getElementById('modalEditarPerfil'));
    modalPassword = new bootstrap.Modal(document.getElementById('modalPassword'));

    const sesion = JSON.parse(localStorage.getItem("usuario"));
    if (!sesion) { window.location.href = "login.html"; return; }

    document.getElementById("rolTitulo").innerText = sesion.rolName;
    document.getElementById("usuarioPerfil").innerText = sesion.usuario;

    await cargarDatosDeCuenta(sesion.idUsuario);

    const esCliente = sesion.rolName.toUpperCase() === "CLIENTE";
    const urlBase = esCliente ? API_CLIENTE : API_EMPLEADO;
    
    await cargarPerfilPersonal(urlBase, sesion.idPersona);
    
    if (esCliente) document.getElementById("cardAccesoPedidos").style.display = "block";
});



async function cargarDatosDeCuenta(idUsuario) {
    try {
        const res = await fetch(`${API_USUARIO}/buscar/${idUsuario}`);
        if (res.ok) {
            const u = await res.json();
            document.getElementById("emailPerfil").innerText = u.email;
            const badge = document.getElementById("estadoPerfil");
            badge.innerText = u.estado;
            badge.className = u.estado === 'Activo' ? 'badge bg-success' : 'badge bg-danger';
        }
    } catch (e) { console.error(e); }
}

async function cargarPerfilPersonal(urlBase, idPersona) {
    try {
        const res = await fetch(`${urlBase}/buscar/${idPersona}`);
        if (res.ok) {
            datosCargados = await res.json();
            document.getElementById("nombreCompleto").innerText = `${datosCargados.nombre} ${datosCargados.apellido}`;
            document.getElementById("dniPerfil").innerText = datosCargados.dni || "---";
            document.getElementById("telefonoPerfil").innerText = datosCargados.telefono || "---";
            document.getElementById("direccionPerfil").innerText = datosCargados.direccion || "---";
        }
    } catch (e) { console.error(e); }
}



window.abrirModalEditar = () => {
    document.getElementById("editNombre").value = datosCargados.nombre || "";
    document.getElementById("editApellido").value = datosCargados.apellido || "";
    document.getElementById("editDni").value = datosCargados.dni || "";
    document.getElementById("editTelefono").value = datosCargados.telefono || "";
    document.getElementById("editDireccion").value = datosCargados.direccion || "";
    
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    document.getElementById("editUsuario").value = sesion.usuario;
    document.getElementById("editEmail").value = document.getElementById("emailPerfil").innerText;
    modalEditar.show();
};

document.getElementById("formEditarPerfil").addEventListener("submit", async (e) => {
    e.preventDefault();
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    const urlBase = sesion.rolName.toUpperCase() === "CLIENTE" ? API_CLIENTE : API_EMPLEADO;

    const personaBody = {
        nombre: document.getElementById("editNombre").value,
        apellido: document.getElementById("editApellido").value,
        dni: document.getElementById("editDni").value,
        telefono: document.getElementById("editTelefono").value,
        direccion: document.getElementById("editDireccion").value
    };

    const usuarioBody = {
        idUsuario: sesion.idUsuario,
        usuario: document.getElementById("editUsuario").value,
        email: document.getElementById("editEmail").value
    };

    try {
        const [resP, resU] = await Promise.all([
            fetch(`${urlBase}/actualizar/${sesion.idPersona}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(personaBody)
            }),
            fetch(`${API_USUARIO}/actualizar`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(usuarioBody)
            })
        ]);

        if (resP.ok && resU.ok) {
            alert("Perfil actualizado");
            sesion.usuario = usuarioBody.usuario;
            localStorage.setItem("usuario", JSON.stringify(sesion));
            location.reload();
        }
    } catch (e) { alert("Error al actualizar"); }
});



window.abrirModalPassword = () => {
    document.getElementById("formPassword").reset();
    modalPassword.show();
};

document.getElementById("formPassword").addEventListener("submit", async (e) => {
    e.preventDefault();
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    const passNueva = document.getElementById("passNueva").value;
    const passConfirmar = document.getElementById("passConfirmar").value;

    if (passNueva !== passConfirmar) return alert("Las contraseñas no coinciden");

    const dto = {
        idUsuario: sesion.idUsuario,
        oldPassword: document.getElementById("passActual").value,
        newPassword: passNueva
    };

    try {
        const res = await fetch(`${API_USUARIO}/actualizar-password`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dto)
        });

        if (res.ok) {
            alert("Contraseña cambiada correctamente");
            modalPassword.hide();
        } else {
            const msg = await res.text();
            alert("Error: " + msg);
        }
    } catch (e) { alert("Error de conexión"); }
});