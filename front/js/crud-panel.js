const CrudPanel={

api:"",
tabla:"",
idCampo:"",
nombreCampo:"",
datos:[],
paginaActual:1,
filasPorPagina:5,
ordenAsc:true,

init(config){

this.api=config.api
this.tabla=config.tabla
this.idCampo=config.idCampo
this.nombreCampo=config.nombreCampo

this.listar()

},


mostrarLoader(){

document.getElementById("loader").style.display="flex"

},

ocultarLoader(){

document.getElementById("loader").style.display="none"

},

listar(){

this.mostrarLoader()

fetch(this.api+"/listar")

.then(res=>res.json())

.then(data=>{

this.datos=data

this.renderTabla()

this.renderPaginacion()

this.ocultarLoader()

})

},

renderTabla(){

const tbody=document.querySelector(this.tabla+" tbody")

tbody.innerHTML=""

const inicio=(this.paginaActual-1)*this.filasPorPagina

const fin=inicio+this.filasPorPagina

const datosPagina=this.datos.slice(inicio,fin)

datosPagina.forEach(item=>{

let badge=item.estado==="Activo"
? `<span class="badge badge-activo">Activo</span>`
: `<span class="badge badge-inactivo">Inactivo</span>`

let botonEstado=item.estado==="Activo"

? `<button class="btn-desactivar" onclick="CrudPanel.confirmar('desactivar',${item[this.idCampo]})">
<i class="fa fa-ban"></i>
</button>`

: `<button class="btn-activar" onclick="CrudPanel.confirmar('activar',${item[this.idCampo]})">
<i class="fa fa-check"></i>
</button>`

const fila=document.createElement("tr")

fila.innerHTML=`

<td>${item[this.idCampo]}</td>

<td>${item[this.nombreCampo]}</td>

<td>${badge}</td>

<td>

<button class="btn-editar" onclick="CrudPanel.abrirEditar(${item[this.idCampo]})">
<i class="fa fa-pen"></i>
</button>

${botonEstado}

</td>

`

tbody.appendChild(fila)

})

},

renderPaginacion(){

const totalPaginas=Math.ceil(this.datos.length/this.filasPorPagina)

const cont=document.getElementById("paginacion")

cont.innerHTML=""

for(let i=1;i<=totalPaginas;i++){

const btn=document.createElement("button")

btn.textContent=i

btn.onclick=()=>{

this.paginaActual=i

this.renderTabla()

}

cont.appendChild(btn)

}

},

ordenar(campo){

this.ordenAsc=!this.ordenAsc

this.datos.sort((a,b)=>{

if(a[campo]>b[campo]) return this.ordenAsc?1:-1

if(a[campo]<b[campo]) return this.ordenAsc?-1:1

return 0

})

this.renderTabla()

},

filtrar(valor){

valor=valor.toLowerCase()

this.datos=this.datos.filter(d=>

d[this.nombreCampo].toLowerCase().includes(valor)

)

this.paginaActual=1

this.renderTabla()

this.renderPaginacion()

},

confirmar(tipo,id){

const texto=tipo==="desactivar"
?"¿Desea desactivar este registro?"
:"¿Desea activar este registro?"

if(!confirm(texto)) return

tipo==="activar"
?this.activar(id)
:this.desactivar(id)

},


activar(id){

this.mostrarLoader()

fetch(`${this.api}/activar/${id}`,{method:"PUT"})
.then(()=>{

this.toast("Registro activado")

this.listar()

})

},

desactivar(id){

this.mostrarLoader()

fetch(`${this.api}/desactivar/${id}`,{method:"PUT"})
.then(()=>{

this.toast("Registro desactivado")

this.listar()

})

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

this.mostrarLoader()

fetch(`${this.api}/actualizar/${id}`,{

method:"PUT",

headers:{"Content-Type":"application/json"},

body:JSON.stringify({nombre})

})

.then(()=>{

document.getElementById("modalEditar").style.display="none"

this.toast("Registro actualizado")

this.listar()

})

},


guardarNuevo(){

const nombre=document.getElementById("createNombre").value

this.mostrarLoader()

fetch(`${this.api}/crear`,{

method:"POST",

headers:{"Content-Type":"application/json"},

body:JSON.stringify({nombre})

})

.then(()=>{

document.getElementById("modalCrear").style.display="none"

this.toast("Registro creado")

this.listar()

})

},


toast(msg){

let t=document.createElement("div")

t.className="toast"

t.innerText=msg

document.body.appendChild(t)

setTimeout(()=>t.classList.add("show"),100)

setTimeout(()=>{

t.classList.remove("show")

setTimeout(()=>t.remove(),300)

},2000)

}

}