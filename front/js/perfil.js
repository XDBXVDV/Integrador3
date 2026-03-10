const API_CLIENTE = "http://localhost:8080/cliente";

document.addEventListener("DOMContentLoaded", () => {

    const usuarioStorage = localStorage.getItem("usuario");

    if (!usuarioStorage) {
        window.location.href = "login.html";
        return;
    }

    const usuario = JSON.parse(usuarioStorage);

    cargarDatosUsuario(usuario);

    if (usuario.rol.rolName.toUpperCase() === "CLIENTE") {
        cargarDatosCliente(usuario.idUsuario);
    }

});



function cargarDatosUsuario(usuario){

    document.getElementById("rolTitulo").innerText =
        usuario.rol.rolName.toUpperCase();

    document.getElementById("usuarioPerfil").innerText =
        usuario.usuario ?? "";

    document.getElementById("emailPerfil").innerText =
        usuario.email ?? "";

    document.getElementById("estadoPerfil").innerText =
        usuario.estado ?? "";

}



async function cargarDatosCliente(idUsuario){

    try{

        const res = await fetch(`${API_CLIENTE}/usuario/${idUsuario}`);

        if(!res.ok){
            throw new Error("No se pudo cargar el cliente");
        }

        const c = await res.json();

        document.getElementById("nombreCompleto").innerText =
            (c.nombre ?? "") + " " + (c.apellido ?? "");

        document.getElementById("dniPerfil").innerText =
            c.dni ?? "";

        document.getElementById("telefonoPerfil").innerText =
            c.telefono ?? "";

        document.getElementById("direccionPerfil").innerText =
            c.direccion ?? "";

    }catch(error){

        console.error(error);

        document.getElementById("nombreCompleto").innerText =
            "Cliente";

    }

}