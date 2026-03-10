let contenedor
let filtroCategoria
let filtroMarca

let productos = []

document.addEventListener("DOMContentLoaded", () => {

    contenedor = document.getElementById("contenedorProductos")
    filtroCategoria = document.getElementById("filtroCategoria")
    filtroMarca = document.getElementById("filtroMarca")

    cargarProductos()

})


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

        const imagen = p.imagen
        ? "http://localhost:8080" + p.imagen
        : "http://localhost:8080/img/productos/default.png"

        card.innerHTML = `

<img src="${imagen}" class="img-producto">

<h3>${p.nombre}</h3>

<p class="precio">S/ ${p.precio}</p>

<p>${p.categoria}</p>

<p>${p.marca}</p>

<button onclick="verDetalle(${p.idProducto})">
Ver producto
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


/* ---------------- DETALLE ---------------- */

function verDetalle(id){

    window.location.href = "detalle_producto.html?id=" + id

}