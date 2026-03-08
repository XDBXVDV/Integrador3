const contenedor = document.getElementById("detalleProducto")

document.addEventListener("DOMContentLoaded", cargarProducto)

function obtenerId(){

    const params = new URLSearchParams(window.location.search)

    return params.get("id")

}

async function cargarProducto(){

    const id = obtenerId()

    const res = await fetch("http://localhost:8080/producto/listarDTO")

    const productos = await res.json()

    const p = productos.find(prod => prod.idProducto == id)

    if(!p){

        contenedor.innerHTML = "<p>Producto no encontrado</p>"
        return

    }

    const imagen = p.imagen
    ? "http://localhost:8080" + p.imagen
    : "http://localhost:8080/img/productos/default.png"

    contenedor.innerHTML = `

        <img src="${imagen}" width="300">

        <h2>${p.nombre}</h2>

        <p>${p.descripcion}</p>

        <p>Precio: S/ ${p.precio}</p>

        <p>Stock: ${p.stock}</p>

        <input type="number" min="1" max="${p.stock}" value="1" id="cantidad">

        <button onclick="agregarCarrito(${p.idProducto})">
        Agregar al carrito
        </button>

    `

}

function agregarCarrito(id){

    const cantidad = document.getElementById("cantidad").value

    alert("Producto agregado ID: " + id + " Cantidad: " + cantidad)

}