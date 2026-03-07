const API_URL = "http://localhost:8080/producto";

const modalProducto = new bootstrap.Modal(document.getElementById('modalProducto'));
const form = document.getElementById('formProducto');

document.addEventListener("DOMContentLoaded", () => {

    listarProductos();
    cargarCombos();

});


/* =========================
   PREVIEW IMAGEN
========================= */

document.getElementById("imagen").addEventListener("change", function(){

    const file = this.files[0];
    const preview = document.getElementById("imagenPreview");

    if(file){

        const reader = new FileReader();

        reader.onload = function(e){
            preview.src = e.target.result;
            preview.style.display = "block";
        }

        reader.readAsDataURL(file);
    }

});


/* =========================
   LISTAR PRODUCTOS
========================= */

async function listarProductos(){

try{

const resp = await fetch(`${API_URL}/listar`);
const productos = await resp.json();

const tabla = document.getElementById("tablaProductos");
tabla.innerHTML="";

productos.forEach(p=>{

let botonEstado="";

if(p.estado==="Activo"){

botonEstado=`
<button class="btn btn-danger btn-sm"
onclick="desactivarProducto(${p.idProducto})">
Desactivar
</button>
`;

}else{

botonEstado=`
<button class="btn btn-success btn-sm"
onclick="reactivarProducto(${p.idProducto})">
Reactivar
</button>
`;

}

tabla.innerHTML+=`

<tr>

<td>${p.idProducto}</td>

<td>
<img src="${p.imagen ? 'http://localhost:8080'+p.imagen : 'https://via.placeholder.com/60'}"
class="img-producto">
</td>

<td>${p.nombre}</td>

<td>${p.categoria ? p.categoria.nombre : ""}</td>

<td>${p.marca ? p.marca.nombre : ""}</td>

<td>S/ ${p.precio.toFixed(2)}</td>

<td>${p.stock}</td>

<td>${p.stockMinimo}</td>

<td>
<span class="badge bg-secondary">
${p.estado}
</span>
</td>

<td>
<span class="badge badge-${p.condicion}">
${p.condicion.replace('_',' ')}
</span>
</td>

<td>

${botonEstado}

<button class="btn btn-warning btn-sm"
onclick="prepararEdicion(${p.idProducto})">
Editar
</button>

</td>

</tr>

`;

});

}catch(error){

console.error("Error al listar:",error);

}

}


/* =========================
   ABRIR MODAL CREAR
========================= */

function abrirModalCrear(){

form.reset();

document.getElementById("idProducto").value="";
document.getElementById("imagenPreview").style.display="none";

document.getElementById("modalTitulo").innerText="Nuevo Producto";

modalProducto.show();

}


/* =========================
   EDITAR PRODUCTO
========================= */

async function prepararEdicion(id){

try{

const resp=await fetch(`${API_URL}/buscar/${id}`);
const p=await resp.json();

document.getElementById("idProducto").value=p.idProducto;
document.getElementById("nombre").value=p.nombre;
document.getElementById("descripcion").value=p.descripcion ?? "";
document.getElementById("precio").value=p.precio;
document.getElementById("stock").value=p.stock;
document.getElementById("stockMinimo").value=p.stockMinimo;
document.getElementById("idCategoria").value=p.categoria.idCategoria;
document.getElementById("idMarca").value=p.marca.idMarca;
document.getElementById("estado").value=p.estado;

const preview=document.getElementById("imagenPreview");

if(p.imagen){

preview.src="http://localhost:8080"+p.imagen;
preview.style.display="block";

}else{

preview.style.display="none";

}

document.getElementById("modalTitulo").innerText="Editar Producto";

modalProducto.show();

}catch(error){

alert("Error al cargar producto");

}

}


/* =========================
   GUARDAR PRODUCTO
========================= */

form.onsubmit=async(e)=>{

e.preventDefault();

const id=document.getElementById("idProducto").value;

const formData = new FormData();

formData.append("nombre",document.getElementById("nombre").value);
formData.append("descripcion",document.getElementById("descripcion").value);
formData.append("precio",document.getElementById("precio").value);
formData.append("stock",document.getElementById("stock").value);
formData.append("stockMinimo",document.getElementById("stockMinimo").value);
formData.append("estado",document.getElementById("estado").value);

formData.append("categoriaId",document.getElementById("idCategoria").value);
formData.append("marcaId",document.getElementById("idMarca").value);

const imagenFile = document.getElementById("imagen").files[0];

if(imagenFile){
formData.append("imagen",imagenFile);
}

const url=id?`${API_URL}/actualizar/${id}`:`${API_URL}/crear`;
const method=id?'PUT':'POST';

try{

const resp=await fetch(url,{
method:method,
body:formData
});

if(resp.ok){

alert(id?"Actualizado correctamente":"Creado correctamente");

modalProducto.hide();
listarProductos();

}else{

const err=await resp.text();
alert("Error: "+err);

}

}catch(error){

console.error("Error en petición:",error);

}

};



/* =========================
   DESACTIVAR PRODUCTO
========================= */

function desactivarProducto(id){

if(!confirm("¿Desea desactivar este producto?"))return;

fetch(`${API_URL}/desactivar/${id}`,{
method:"PUT"
})
.then(res=>{
if(!res.ok)throw new Error("Error al desactivar");
listarProductos();
})
.catch(err=>alert(err.message));

}



/* =========================
   REACTIVAR PRODUCTO
========================= */

function reactivarProducto(id){

if(!confirm("¿Desea reactivar este producto?"))return;

fetch(`${API_URL}/activar/${id}`,{
method:"PUT"
})
.then(res=>{
if(!res.ok)throw new Error("Error al reactivar");
listarProductos();
})
.catch(err=>alert(err.message));

}



/* =========================
   CARGAR COMBOS
========================= */

async function cargarCombos(){

try{

const respCat=await fetch(`http://localhost:8080/categoria/listar/activos`);
const categorias=await respCat.json();

const selectCat=document.getElementById("idCategoria");
selectCat.innerHTML='<option value="">Seleccione Categoría...</option>';

categorias.forEach(c=>{

const option=document.createElement("option");
option.value=c.idCategoria;
option.textContent=c.nombre;

selectCat.appendChild(option);

});


const respMarca=await fetch(`http://localhost:8080/marca/listar/activos`);
const marcas=await respMarca.json();

const selectMarca=document.getElementById("idMarca");
selectMarca.innerHTML='<option value="">Seleccione Marca...</option>';

marcas.forEach(m=>{

const option=document.createElement("option");
option.value=m.idMarca;
option.textContent=m.nombre;

selectMarca.appendChild(option);

});

}catch(error){

console.error("Error cargando combos:",error);

}

}