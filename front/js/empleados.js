const API_EMPLEADO = "http://localhost:8080/empleado";
const API_USUARIO = "http://localhost:8080/usuario";

let tablaData;
let idUsuarioGlobal=null;

const modalEmpleado = new bootstrap.Modal(document.getElementById("modalEmpleado"));

document.addEventListener("DOMContentLoaded",()=>{

cargarEmpleados();

});


async function cargarEmpleados(){

if(tablaData){
tablaData.destroy();
}

const res = await fetch(`${API_EMPLEADO}/listar`);
const empleados = await res.json();

const tabla=document.getElementById("tablaEmpleados");

tabla.innerHTML="";

empleados.forEach(e=>{

let botones=`

<button class="btn btn-info btn-sm"
onclick="verDetalle(${e.idEmpleado})">
Ver
</button>

`;

if(e.estadoUsuario==="Activo"){

botones+=`
<button class="btn btn-danger btn-sm"
onclick="desactivarTabla(${e.idUsuario})">
Desactivar
</button>
`;

}else{

botones+=`
<button class="btn btn-success btn-sm"
onclick="activar(${e.idUsuario})">
Activar
</button>
`;

}

tabla.innerHTML+=`

<tr>

<td>${e.nombre} ${e.apellido}</td>
<td>${e.dni ?? ""}</td>
<td>${e.rol}</td>

<td>
<span class="badge bg-secondary">
${e.estadoUsuario}
</span>
</td>

<td>${botones}</td>

</tr>

`;

});

tablaData=new DataTable('#tablaEmpleadosData',{
pageLength:10,
language:{
url:'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
}
});

}



function abrirModalCrear(){

document.getElementById("formEmpleado").reset();

document.getElementById("idEmpleado").value="";

document.getElementById("modalTitulo").innerText="Nuevo Empleado";

modalEmpleado.show();

}



async function verDetalle(idEmpleado){

const res = await fetch(`${API_EMPLEADO}/buscar/${idEmpleado}`);
const e = await res.json();

document.getElementById("idEmpleado").value=e.idEmpleado;

document.getElementById("nombre").value=e.nombre;
document.getElementById("apellido").value=e.apellido;
document.getElementById("dni").value=e.dni ?? "";

document.getElementById("usuario").value=e.usuario;
document.getElementById("email").value=e.email;

idUsuarioGlobal=e.idUsuario;

document.getElementById("modalTitulo").innerText="Editar Empleado";

modalEmpleado.show();

}



document.getElementById("formEmpleado").addEventListener("submit",async(e)=>{

e.preventDefault();

const idEmpleado=document.getElementById("idEmpleado").value;

if(!idEmpleado){

await fetch(`${API_EMPLEADO}/crear`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

usuario:document.getElementById("usuario").value,
email:document.getElementById("email").value,
contrasena:document.getElementById("password").value,
idRol:Number(document.getElementById("rol").value),

nombre:document.getElementById("nombre").value,
apellido:document.getElementById("apellido").value,
dni:document.getElementById("dni").value

})

});

alert("Empleado creado");

}else{

await fetch(`${API_EMPLEADO}/actualizar/${idEmpleado}`,{

method:"PUT",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

nombre:document.getElementById("nombre").value,
apellido:document.getElementById("apellido").value,
dni:document.getElementById("dni").value

})

});


await fetch(`${API_USUARIO}/actualizar`,{

method:"PUT",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

idUsuario:idUsuarioGlobal,
usuario:document.getElementById("usuario").value,
email:document.getElementById("email").value

})

});

alert("Empleado actualizado");

}

modalEmpleado.hide();

cargarEmpleados();

});



async function desactivarTabla(idUsuario){

if(!confirm("¿Desactivar empleado?")) return;

await fetch(`${API_USUARIO}/desactivar/${idUsuario}`,{method:"PUT"});

cargarEmpleados();

}



async function activar(idUsuario){

if(!confirm("¿Activar empleado?")) return;

await fetch(`${API_USUARIO}/reactivar/${idUsuario}`,{method:"PUT"});

cargarEmpleados();

}



async function desactivarUsuario(){

if(!confirm("¿Desactivar empleado?")) return;

await fetch(`${API_USUARIO}/desactivar/${idUsuarioGlobal}`,{method:"PUT"});

modalEmpleado.hide();

cargarEmpleados();

}