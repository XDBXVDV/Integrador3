const API_URL = "http://localhost:8080/marca";

document.addEventListener("DOMContentLoaded", () => {
    listarMarcas();
});


/* =========================
   LISTAR MARCAS
========================= */

function listarMarcas(){

fetch(API_URL + "/listar")

.then(res=>{
if(!res.ok) throw new Error("Error al obtener marcas")
return res.json()
})

.then(data=>{

const tbody = document.querySelector("#tablaMarcas tbody")

tbody.innerHTML=""

data.forEach(m=>{

let botonEstado=""

if(m.estado==="Activo"){

botonEstado=`
<button onclick="desactivarMarca(${m.idMarca})">
Desactivar
</button>
`

}else{

botonEstado=`
<button onclick="reactivarMarca(${m.idMarca})">
Reactivar
</button>
`

}

const fila=document.createElement("tr")

fila.innerHTML=`

<td>${m.idMarca}</td>
<td>${m.nombre}</td>
<td>${m.estado}</td>

<td>

<button onclick="abrirModalEditar(${m.idMarca})">
Editar
</button>

${botonEstado}

</td>

`

tbody.appendChild(fila)

})

})

.catch(err=>alert(err.message))

}


/* =========================
   DESACTIVAR
========================= */

function desactivarMarca(id){

if(!confirm("¿Desea desactivar esta marca?")) return

fetch(`${API_URL}/desactivar/${id}`,{
method:"PUT"
})

.then(res=>{
if(!res.ok) throw new Error("Error al desactivar")
listarMarcas()
})

.catch(err=>alert(err.message))

}


/* =========================
   REACTIVAR
========================= */

function reactivarMarca(id){

if(!confirm("¿Desea reactivar esta marca?")) return

fetch(`${API_URL}/activar/${id}`,{
method:"PUT"
})

.then(res=>{
if(!res.ok) throw new Error("Error al reactivar")
listarMarcas()
})

.catch(err=>alert(err.message))

}


/* =========================
   ABRIR MODAL EDITAR
========================= */

function abrirModalEditar(id){

fetch(`${API_URL}/buscar/${id}`)

.then(res=>{
if(!res.ok) throw new Error("No se pudo obtener marca")
return res.json()
})

.then(m=>{

document.getElementById("editIdMarca").value=m.idMarca

document.getElementById("editNombre").value=m.nombre

document.getElementById("modalEditar").style.display="flex"

})

.catch(err=>alert(err.message))

}


/* =========================
   CERRAR MODAL EDITAR
========================= */

function cerrarModal(){

document.getElementById("modalEditar").style.display="none"

}


/* =========================
   GUARDAR EDICION
========================= */

function guardarEdicion(){

const id=document.getElementById("editIdMarca").value

const data={
nombre:document.getElementById("editNombre").value
}

fetch(`${API_URL}/actualizar/${id}`,{

method:"PUT",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify(data)

})

.then(res=>{

if(!res.ok) throw new Error("Error al actualizar marca")

cerrarModal()

listarMarcas()

})

.catch(err=>alert(err.message))

}


/* =========================
   ABRIR MODAL CREAR
========================= */

function abrirModalCrear(){

document.getElementById("modalCrear").style.display="flex"

}


/* =========================
   CERRAR MODAL CREAR
========================= */

function cerrarModalCrear(){

document.getElementById("modalCrear").style.display="none"

limpiarFormularioCrear()

}


/* =========================
   GUARDAR MARCA
========================= */

function guardarMarca(){

const nombre=document.getElementById("createNombre").value

if(!nombre){

alert("El nombre es obligatorio")

return

}

const data={nombre}

fetch(`${API_URL}/crear`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify(data)

})

.then(res=>{

if(!res.ok) throw new Error("Error al crear marca")

cerrarModalCrear()

listarMarcas()

})

.catch(err=>alert(err.message))

}


/* =========================
   LIMPIAR FORMULARIO
========================= */

function limpiarFormularioCrear(){

document.getElementById("createNombre").value=""

}


/* =========================
   BUSCADOR
========================= */

function filtrarTabla(){

const input=document.getElementById("buscadorTabla")

const filtro=input.value.toLowerCase()

const filas=document.querySelectorAll("#tablaMarcas tbody tr")

filas.forEach(fila=>{

const texto=fila.innerText.toLowerCase()

if(texto.includes(filtro)){

fila.style.display=""

}else{

fila.style.display="none"

}

})

}


/* =========================
   CERRAR MODALES CLICK FUERA
========================= */

window.onclick=function(event){

const modalCrear=document.getElementById("modalCrear")

const modalEditar=document.getElementById("modalEditar")

if(event.target===modalCrear){

modalCrear.style.display="none"

}

if(event.target===modalEditar){

modalEditar.style.display="none"

}

}