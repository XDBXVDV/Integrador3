document.addEventListener("DOMContentLoaded", () => {

    cargarNavbar();
    activarDropdown();

});

function getUsuarioSesion(){

    const usuario = localStorage.getItem("usuario");

    if(!usuario) return null;

    return JSON.parse(usuario);

}

function cargarNavbar(){

    const usuario = getUsuarioSesion();

    const usuarioInfo = document.getElementById("usuarioInfo");
    const btnLogin = document.getElementById("btnLogin");
    const btnLogout = document.getElementById("btnLogout");
    const areaEmpleado = document.getElementById("areaEmpleado");
    const perfilLink = document.getElementById("perfilLink");

    if(!usuario){

        usuarioInfo.innerText="Invitado";

        btnLogin.style.display="block";
        btnLogout.style.display="none";
        perfilLink.style.display="none";
        areaEmpleado.style.display="none";

        return;
    }

    usuarioInfo.innerText = usuario.usuario + " (" + usuario.rol.rolName + ")";

    btnLogin.style.display="none";
    btnLogout.style.display="block";
    perfilLink.style.display="block";

    const rol = usuario.rol.rolName.toUpperCase();

    if(rol !== "CLIENTE"){

        areaEmpleado.style.display="inline";

    }else{

        areaEmpleado.style.display="none";

    }

}

function activarDropdown(){

    const btn = document.getElementById("btnUsuario");
    const dropdown = document.getElementById("dropdownUsuario");

    btn.addEventListener("click", ()=>{

        if(dropdown.style.display === "block"){

            dropdown.style.display="none";

        }else{

            dropdown.style.display="block";

        }

    });

}

function logout(){

    localStorage.removeItem("usuario");

    window.location.href="index.html";

}