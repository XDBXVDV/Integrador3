const API_CLIENTES = "http://localhost:8080/cliente";
const API_USUARIO = "http://localhost:8080/usuario";

let tablaData;
let idUsuarioGlobal=null;

const modalCliente = new bootstrap.Modal(document.getElementById("modalCliente"));

document.addEventListener("DOMContentLoaded",()=>{

listarClientes();

});


async function listarClientes(){

if(tablaData){
tablaData.destroy();
}

const response = await fetch(`${API_CLIENTES}/listar`);
const clientes = await response.json();

const tabla = document.getElementById("tablaClientes");

tabla.innerHTML="";

clientes.forEach(c=>{

let botones=`

<button class="btn btn-info btn-sm"
onclick="verDetalle(${c.idCliente})">
Detalles
</button>

`;

if(c.estadoUsuario==="Activo"){

botones+=`
<button class="btn btn-danger btn-sm"
onclick="desactivarUsuarioTabla(${c.idUsuario})">
Desactivar
</button>
`;

}else{

botones+=`
<button class="btn btn-success btn-sm"
onclick="activarUsuario(${c.idUsuario})">
Activar
</button>
`;

}

tabla.innerHTML+=`

<tr>

<td>${c.nombre} ${c.apellido}</td>
<td>${c.dni ?? ""}</td>
<td>${c.telefono ?? ""}</td>

<td>
<span class="badge bg-secondary">
${c.estadoUsuario}
</span>
</td>

<td>${botones}</td>

</tr>

`;

});

tablaData = new DataTable('#tablaClientesData',{
pageLength:10,
language:{
url:'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
}
});

}



function abrirModalCrear(){

document.getElementById("formCliente").reset();
document.getElementById("idCliente").value="";

document.getElementById("modalTitulo").innerText="Nuevo Cliente";

modalCliente.show();

}



async function verDetalle(idCliente){

const response = await fetch(`${API_CLIENTES}/buscar/${idCliente}`);
const c = await response.json();

document.getElementById("idCliente").value=c.idCliente;

document.getElementById("nombre").value=c.nombre;
document.getElementById("apellido").value=c.apellido;
document.getElementById("dni").value=c.dni ?? "";
document.getElementById("telefono").value=c.telefono ?? "";
document.getElementById("direccion").value=c.direccion ?? "";

document.getElementById("usuario").value=c.usuario;
document.getElementById("email").value=c.email;

idUsuarioGlobal=c.idUsuario;

document.getElementById("modalTitulo").innerText="Editar Cliente";

modalCliente.show();

}



document.getElementById("formCliente").addEventListener("submit",async(e)=>{

e.preventDefault();

const idCliente=document.getElementById("idCliente").value;

if(!idCliente){

await fetch(`${API_CLIENTES}/crear`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

usuario:document.getElementById("usuario").value,
email:document.getElementById("email").value,
contrasena:document.getElementById("contrasena").value,
idRol:1,

nombre:document.getElementById("nombre").value,
apellido:document.getElementById("apellido").value,
dni:document.getElementById("dni").value,
telefono:document.getElementById("telefono").value,
direccion:document.getElementById("direccion").value

})

});

alert("Cliente creado");

}else{

await fetch(`${API_CLIENTES}/actualizar/${idCliente}`,{

method:"PUT",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({

nombre:document.getElementById("nombre").value,
apellido:document.getElementById("apellido").value,
dni:document.getElementById("dni").value,
telefono:document.getElementById("telefono").value,
direccion:document.getElementById("direccion").value

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

alert("Cliente actualizado");

}

modalCliente.hide();

listarClientes();

});



async function desactivarUsuarioTabla(idUsuario){

if(!confirm("¿Desactivar cliente?")) return;

await fetch(`${API_USUARIO}/desactivar/${idUsuario}`,{method:"PUT"});

listarClientes();

}



async function activarUsuario(idUsuario){

if(!confirm("¿Activar cliente?")) return;

await fetch(`${API_USUARIO}/reactivar/${idUsuario}`,{method:"PUT"});

listarClientes();

}



async function desactivarUsuario(){

if(!confirm("¿Desactivar cliente?")) return;

await fetch(`${API_USUARIO}/desactivar/${idUsuarioGlobal}`,{method:"PUT"});

modalCliente.hide();

listarClientes();

}