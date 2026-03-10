document.addEventListener("DOMContentLoaded", async () => {

    const container = document.getElementById("navbarContainer")

    if(!container) return

    const res = await fetch("../components/navbarclientes.html")

    const html = await res.text()

    container.innerHTML = html

    iniciarNavbar()

})


function getUsuarioSesion(){

    const usuario = localStorage.getItem("usuario")

    if(!usuario) return null

    return JSON.parse(usuario)

}


function iniciarNavbar(){

    const usuario = getUsuarioSesion()

    const usuarioInfo = document.getElementById("usuarioInfo")
    const btnLogin = document.getElementById("btnLogin")
    const btnLogout = document.getElementById("btnLogout")
    const areaEmpleado = document.getElementById("areaEmpleado")

    if(!usuario){

        usuarioInfo.innerText = "Invitado"

        btnLogin.style.display = "inline"
        btnLogout.style.display = "none"
        areaEmpleado.style.display = "none"

        return
    }

    usuarioInfo.innerText = usuario.usuario + " (" + usuario.rol.rolName + ")"

    btnLogin.style.display = "none"
    btnLogout.style.display = "inline"

    const rol = usuario.rol.rolName.toUpperCase()

    if(rol !== "CLIENTE"){
        areaEmpleado.style.display = "inline"
    }else{
        areaEmpleado.style.display = "none"
    }

}


function logout(){

    localStorage.removeItem("usuario")

    window.location.href = "login.html"

}