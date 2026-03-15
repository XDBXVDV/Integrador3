const API_BASE = "http://localhost:8080/api/pedidos-compra";
const API_PRODUCTOS = "http://localhost:8080/producto/listar";
let itemsRequerimiento = [];

document.addEventListener("DOMContentLoaded", () => {
    cargarProductos();
});

async function cargarProductos() {
    const res = await fetch(API_PRODUCTOS);
    const productos = await res.json();
    const select = document.getElementById("selectProducto");
    select.innerHTML = '<option value="">-- Seleccione un producto --</option>';
    productos.forEach(p => {
        select.innerHTML += `<option value="${p.idProducto}">${p.nombre}</option>`;
    });
}

window.agregarItem = function() {
    const select = document.getElementById("selectProducto");
    const idProd = select.value;
    const nombreProd = select.options[select.selectedIndex].text;
    const cant = parseInt(document.getElementById("inputCantidad").value);

    if (!idProd || isNaN(cant) || cant <= 0) return alert("Seleccione un producto y cantidad válida");

    // Verificar si ya existe en la lista
    const existe = itemsRequerimiento.find(i => i.idProducto == idProd);
    if (existe) {
        existe.cantidad += cant;
    } else {
        itemsRequerimiento.push({ idProducto: parseInt(idProd), nombre: nombreProd, cantidad: cant });
    }

    actualizarTabla();
}

function actualizarTabla() {
    const tbody = document.getElementById("tablaDetalle");
    tbody.innerHTML = "";
    itemsRequerimiento.forEach((item, index) => {
        tbody.innerHTML += `
            <tr>
                <td>${item.nombre}</td>
                <td class="text-center">${item.cantidad}</td>
                <td class="text-center">
                    <button class="btn btn-sm btn-danger" onclick="eliminarItem(${index})">×</button>
                </td>
            </tr>
        `;
    });
}

window.eliminarItem = (index) => {
    itemsRequerimiento.splice(index, 1);
    actualizarTabla();
}

window.procesarRequerimiento = async function() {
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    if (itemsRequerimiento.length === 0) return alert("La lista está vacía");

    const dto = {
        idEmpleado: parseInt(sesion.idPersona),
        items: itemsRequerimiento.map(i => ({
            idProducto: i.idProducto,
            cantidad: i.cantidad
        }))
    };

    try {
        const res = await fetch(`${API_BASE}/solicitar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });

        if (res.ok) {
            alert("Requerimiento enviado a Compras. Ahora podrá ser cotizado.");
            window.location.href = "gestion_pedidos.html";
        }
    } catch (e) { alert("Error al conectar"); }
}