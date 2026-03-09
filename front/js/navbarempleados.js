async function cargarNavbar(){

const resp = await fetch("../components/navbar.html")

const html = await resp.text()

document.getElementById("navbarContainer").innerHTML = html

}

document.addEventListener("DOMContentLoaded", cargarNavbar)