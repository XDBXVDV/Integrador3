const CrudDinamico = {

api:"",
tabla:"",
idCampo:"",
columnas:[],
formCrear:[],
formEditar:[],

datos:[],


init(config){

this.api=config.api
this.tabla=config.tabla
this.idCampo=config.idCampo
this.columnas=config.columnas
this.formCrear=config.formCrear
this.formEditar=config.formEditar

this.listar()

},


listar(){

fetch(this.api+"/listar")

.then(res=>res.json())

.then(data=>{

this.datos=data

this.renderTabla()

})

},


renderTabla(){

const tbody=document.querySelector(this.tabla+" tbody")

tbody.innerHTML=""

this.datos.forEach(item=>{

let fila=document.createElement("tr")

let html=""

this.columnas.forEach(col=>{

if(col==="estado"){

let badge=item.estado==="Activo"

? `<span class="badge badge-activo">Activo</span>`
: `<span class="badge badge-inactivo">Inactivo</span>`

html+=`<td>${badge}</td>`

}else{

html+=`<td>${item[col]}</td>`

}

})

let botonEstado=""

if(item.estado==="Activo"){

botonEstado=`<button onclick="CrudDinamico.desactivar(${item[this.idCampo]})">Desactivar</button>`

}else{

botonEstado=`<button onclick="CrudDinamico.activar(${item[this.idCampo]})">Activar</button>`

}

html+=`

<td>

<button onclick="CrudDinamico.abrirEditar(${item[this.idCampo]})">
Editar
</button>

${botonEstado}

</td>

`

fila.innerHTML=html

tbody.appendChild(fila)

})

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

this.formEditar.forEach(campo=>{

document.getElementById("edit"+campo).value=data[campo]

})

document.getElementById("modalEditar").style.display="flex"

})

},


guardarEdicion(){

let data={}

this.formEditar.forEach(campo=>{

data[campo]=document.getElementById("edit"+campo).value

})

let id=document.getElementById("edit"+this.idCampo).value

fetch(`${this.api}/actualizar/${id}`,{

method:"PUT",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify(data)

})

.then(()=>{

document.getElementById("modalEditar").style.display="none"

this.listar()

})

},


guardarNuevo(){

let data={}

this.formCrear.forEach(campo=>{

data[campo]=document.getElementById("create"+campo).value

})

fetch(`${this.api}/crear`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify(data)

})

.then(()=>{

document.getElementById("modalCrear").style.display="none"

this.listar()

})

}

}