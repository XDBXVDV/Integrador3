let contenedor
let filtroCategoria
let filtroMarca

let productos = []

document.addEventListener("DOMContentLoaded", () => {

    contenedor = document.getElementById("contenedorProductos")
    filtroCategoria = document.getElementById("filtroCategoria")
    filtroMarca = document.getElementById("filtroMarca")

    cargarNavbar()
    cargarProductos()

})


/* ---------------- NAVBAR ---------------- */

function getUsuarioSesion(){

    const usuario = localStorage.getItem("usuario")

    if(!usuario) return null

    return JSON.parse(usuario)
}

function cargarNavbar(){

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


/* ---------------- PRODUCTOS ---------------- */

async function cargarProductos(){

    try{

        const res = await fetch("http://localhost:8080/producto/listarDTO")

        const data = await res.json()

        productos = data

        llenarFiltros()

        mostrarProductos(productos)

    }catch(error){

        console.error("Error cargando productos:", error)

    }

}


function mostrarProductos(lista){

    contenedor.innerHTML = ""

    lista.forEach(p => {

        if(p.estado !== "Activo") return
        if(p.condicion === "Agotado") return

        const card = document.createElement("div")

        card.classList.add("producto-card")

        const imagen = p.imagen ? p.imagen : "/img/productos/default.png"

        card.innerHTML = `
            <img src="${imagen}" class="img-producto">

            <h3>${p.nombre}</h3>

            <p class="precio">S/ ${p.precio}</p>

            <p class="stock">Stock: ${p.stock}</p>

            <p class="categoria">${p.categoria}</p>

            <p class="marca">${p.marca}</p>

            <input
                type="number"
                min="1"
                max="${p.stock}"
                value="1"
                id="cant-${p.idProducto}"
                onchange="validarCantidad(${p.idProducto}, ${p.stock})"
            >

            <button onclick="agregarCarrito(${p.idProducto})">
                Agregar al carrito
            </button>
        `

        contenedor.appendChild(card)

    })

}


/* ---------------- FILTROS ---------------- */

function llenarFiltros(){

    const categorias = [...new Set(productos.map(p => p.categoria))]
    const marcas = [...new Set(productos.map(p => p.marca))]

    categorias.forEach(c => {

        const op = document.createElement("option")

        op.value = c
        op.textContent = c

        filtroCategoria.appendChild(op)

    })

    marcas.forEach(m => {

        const op = document.createElement("option")

        op.value = m
        op.textContent = m

        filtroMarca.appendChild(op)

    })

    filtroCategoria.addEventListener("change", aplicarFiltros)
    filtroMarca.addEventListener("change", aplicarFiltros)

}

function aplicarFiltros(){

    let lista = productos

    const cat = filtroCategoria.value
    const marca = filtroMarca.value

    if(cat){
        lista = lista.filter(p => p.categoria === cat)
    }

    if(marca){
        lista = lista.filter(p => p.marca === marca)
    }

    mostrarProductos(lista)

}


/* ---------------- VALIDAR CANTIDAD ---------------- */

function validarCantidad(id, stock){

    const input = document.getElementById("cant-" + id)

    if(input.value > stock){
        input.value = stock
    }

    if(input.value < 1){
        input.value = 1
    }

}


/* ---------------- CARRITO (TEMPORAL) ---------------- */

function agregarCarrito(id){

    const cantidad = document.getElementById("cant-" + id).value

    alert("Producto agregado al carrito\nID: " + id + "\nCantidad: " + cantidad)

}