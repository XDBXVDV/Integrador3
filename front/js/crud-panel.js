const CrudPanel = {

api: "",
tabla: "",
idCampo: "",
nombreCampo: "",

init(config){

this.api = config.api
this.tabla = config.tabla
this.idCampo = config.idCampo
this.nombreCampo = config.nombreCampo

this.listar()

},

listar(){

fetch(this.api + "/listar")

.then(res=>{
if(!res.ok) throw new Error("Error al obtener datos")
return res.json()
})

.then(data=>{

const tbody=document.querySelector(this.tabla+" tbody")

tbody.innerHTML=""

data.forEach(item=>{

let botonEstado=""

if(item.estado==="Activo"){

botonEstado=`
<button onclick="CrudPanel.desactivar(${item[this.idCampo]})">
Desactivar
</button>
`

}else{

botonEstado=`
<button onclick="CrudPanel.activar(${item[this.idCampo]})">
Reactivar
</button>
`

}

const fila=document.createElement("tr")

fila.innerHTML=`

<td>${item[this.idCampo]}</td>
<td>${item[this.nombreCampo]}</td>
<td>${item.estado}</td>

<td>

<button onclick="CrudPanel.abrirEditar(${item[this.idCampo]})">
Editar
</button>

${botonEstado}

</td>

`

tbody.appendChild(fila)

})

})

.catch(err=>alert(err.message))

},

activar(id){

fetch(`${this.api}/activar/${id}`,{method:"PUT"})
.then(()=>this.listar())

},

desactivar(id){

fetch(`${this.api}/desactivar/${id}`,{method:"PUT"})
.then(()=>this.listar())

},

abrirEditar(id){

fetch(`${this.api}/buscar/${id}`)

.then(res=>res.json())

.then(data=>{

document.getElementById("editId").value=data[this.idCampo]

document.getElementById("editNombre").value=data[this.nombreCampo]

document.getElementById("modalEditar").style.display="flex"

})

},

guardarEdicion(){

const id=document.getElementById("editId").value

const nombre=document.getElementById("editNombre").value

fetch(`${this.api}/actualizar/${id}`,{

method:"PUT",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({nombre})

})

.then(()=>{

document.getElementById("modalEditar").style.display="none"

this.listar()

})

},

guardarNuevo(){

const nombre=document.getElementById("createNombre").value

fetch(`${this.api}/crear`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({nombre})

})

.then(()=>{

document.getElementById("modalCrear").style.display="none"

this.listar()

})

}

}