const API_URL = "http://localhost:8080/producto";
const modalProducto = new bootstrap.Modal(document.getElementById('modalProducto'));
const form = document.getElementById('formProducto');

document.addEventListener("DOMContentLoaded", listarProductos);


async function listarProductos() {
    try {
        const resp = await fetch(`${API_URL}/listar`);
        const productos = await resp.json();
        
        const tabla = document.getElementById("tablaProductos");
        tabla.innerHTML = "";
let botonEstado = "";


        productos.forEach(p => {

            if (p.estado === "Activo") {
    botonEstado = `
        <button class="btn btn-danger btn-sm" onclick="desactivarProducto(${p.idProducto})">
            Desactivar
        </button>
    `;
} else {
    botonEstado = `
        <button class="btn btn-succes btn-sm" onclick="reactivarProducto(${p.idProducto})">
            Reactivar
        </button>
    `;
}
            tabla.innerHTML += `
                <tr>
                    <td>${p.idProducto}</td>
                    <td>${p.nombre}</td>
                    <td>${p.categoria}</td>
                    <td>${p.marca}</td>
                    <td>S/ ${p.precio.toFixed(2)}</td>
                    <td>${p.stock}</td>
                    <td><span class="badge bg-secondary">${p.estado}</span></td>
                    <td><span class="badge badge-${p.condicion}">${p.condicion.replace('_', ' ')}</span></td>
                    <td>
                         ${botonEstado}
                        <button class="btn btn-warning btn-sm" onclick="prepararEdicion(${p.idProducto})">Editar</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Error al listar:", error);
    }
}


function abrirModalCrear() {
    form.reset();
    document.getElementById('idProducto').value = "";
    document.getElementById('modalTitulo').innerText = "Nuevo Producto";
    modalProducto.show();
}


async function prepararEdicion(id) {
    try {
        const resp = await fetch(`${API_URL}/buscar/${id}`);
        const p = await resp.json();

        document.getElementById('idProducto').value = p.idproducto;
        document.getElementById('nombre').value = p.nombre;
        document.getElementById('precio').value = p.precio;
        document.getElementById('stock').value = p.stock;
        document.getElementById('stockMinimo').value = p.stockMinimo;
        document.getElementById('idCategoria').value = p.categoria.idCategoria;
        document.getElementById('idMarca').value = p.marca.idMarca;
        document.getElementById('estado').value = p.estado;

        document.getElementById('modalTitulo').innerText = "Editar Producto";
        modalProducto.show();
    } catch (error) {
        alert("Error al cargar producto");
    }
}


form.onsubmit = async (e) => {
    e.preventDefault();
    
    const id = document.getElementById('idProducto').value;
    const datos = {
        nombre: document.getElementById('nombre').value,
        precio: parseFloat(document.getElementById('precio').value),
        stock: parseInt(document.getElementById('stock').value),
        stockMinimo: parseInt(document.getElementById('stockMinimo').value),
        estado: document.getElementById('estado').value,
        categoria: { idCategoria: parseInt(document.getElementById('idCategoria').value) },
        marca: { idMarca: parseInt(document.getElementById('idMarca').value) }
    };

    const url = id ? `${API_URL}/actualizar/${id}` : `${API_URL}/crear`;
    const method = id ? 'PUT' : 'POST';

    try {
        const resp = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        if (resp.ok) {
            alert(id ? "Actualizado correctamente" : "Creado correctamente");
            modalProducto.hide();
            listarProductos();
        } else {
            const err = await resp.text();
            alert("Error: " + err);
        }
    } catch (error) {
        console.error("Error en la petición:", error);
    }
};


function desactivarProducto(id) {

    if (!confirm("¿Desea desactivar este producto?")) return;

    fetch(`${API_URL}/desactivar/${id}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al desactivar");
        listarProductos();
    })
    .catch(err => alert(err.message));
}

function reactivarProducto(id) {

    if (!confirm("¿Desea reactivar este producto?")) return;

    fetch(`${API_URL}/activar/${id}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al reactivar");
        listarProductos();
    })
    .catch(err => alert(err.message));
}