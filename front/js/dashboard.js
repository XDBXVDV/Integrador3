function cargarTotales(){

fetch("http://localhost:8080/producto/listar")
.then(res=>res.json())
.then(data=>{

document.getElementById("totalProductos").innerText=data.length

})


fetch("http://localhost:8080/categoria/listar")
.then(res=>res.json())
.then(data=>{

document.getElementById("totalCategorias").innerText=data.length

})


fetch("http://localhost:8080/marca/listar")
.then(res=>res.json())
.then(data=>{

document.getElementById("totalMarcas").innerText=data.length

})


fetch("http://localhost:8080/proveedor/listar")
.then(res=>res.json())
.then(data=>{

document.getElementById("totalProveedores").innerText=data.length

})

}

cargarTotales()