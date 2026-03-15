const idPedido = localStorage.getItem("idPedidoParaCotizar");
const API_PEDIDOS = "http://localhost:8080/api/pedidos-compra";

// CORREGIDO: Ruta directa a tu ProveedorController
const API_PROV = "http://localhost:8080/proveedor/activos"; 

const API_COTIZACION = "http://localhost:8080/api/cotizaciones";

let pedidoData = null;

document.addEventListener("DOMContentLoaded", () => {
    if (!idPedido) {
        alert("No se seleccionó un pedido.");
        window.location.href = "gestion_pedidos.html";
        return;
    }
    document.getElementById("nroPedido").innerText = idPedido;
    cargarProveedores();
    cargarDatosPedido();
});

// --- CARGAR PROVEEDORES (CORREGIDO) ---
async function cargarProveedores() {
    try {
        const res = await fetch(API_PROV);
        if (!res.ok) throw new Error("Error al obtener proveedores");

        const proveedores = await res.json();
        const select = document.getElementById("selectProveedor");
        
        select.innerHTML = '<option value="">-- Seleccione un proveedor --</option>';
        proveedores.forEach(p => {
            // Asegúrate de que el ID en tu entidad Proveedor sea idProveedor
            select.innerHTML += `<option value="${p.idProveedor}">${p.razonSocial}</option>`;
        });
    } catch (error) {
        console.error("Error:", error);
        document.getElementById("selectProveedor").innerHTML = '<option value="">Error al cargar proveedores</option>';
    }
}

// --- CARGAR DATOS DEL PEDIDO ---
async function cargarDatosPedido() {
    try {
        const res = await fetch(`${API_PEDIDOS}/${idPedido}`);
        if (!res.ok) throw new Error("No se pudo cargar el pedido");
        
        pedidoData = await res.json();
        const tbody = document.getElementById("tablaCotizar");
        tbody.innerHTML = "";

        pedidoData.detalles.forEach((det, index) => {
            tbody.innerHTML += `
                <tr>
                    <td>${det.producto.nombre}</td>
                    <td class="text-center">${det.cantidad}</td>
                    <td>
                        <input type="number" class="form-control text-center input-precio" 
                               data-index="${index}" step="0.01" min="0" value="0"
                               oninput="recalcularTotales()">
                    </td>
                    <td class="text-end fw-bold">S/ <span class="subtotal-item">0.00</span></td>
                </tr>
            `;
        });
    } catch (error) {
        alert("Error al cargar datos del pedido.");
    }
}

// --- RECALCULAR TOTALES ---
window.recalcularTotales = function() {
    const inputs = document.querySelectorAll(".input-precio");
    const subtotales = document.querySelectorAll(".subtotal-item");
    let totalGeneral = 0;

    inputs.forEach((input, i) => {
        const precio = parseFloat(input.value) || 0;
        const cant = pedidoData.detalles[i].cantidad;
        const sub = precio * cant;
        subtotales[i].innerText = sub.toFixed(2);
        totalGeneral += sub;
    });

    document.getElementById("totalCotizacion").innerText = totalGeneral.toFixed(2);
}

// --- ENVIAR COTIZACIÓN AL BACKEND ---
window.enviarCotizacion = async function() {
    const idProv = document.getElementById("selectProveedor").value;
    if (!idProv) return alert("Seleccione un proveedor");

    const inputs = document.querySelectorAll(".input-precio");
    
    // Construcción del array de items para el DTO
    const items = pedidoData.detalles.map((det, i) => ({
        idProducto: det.producto.idProducto,
        cantidad: det.cantidad,
        precioUnitario: parseFloat(inputs[i].value) || 0
    }));

    // Validación de precios
    if (items.some(i => i.precioUnitario <= 0)) {
        if (!confirm("Hay productos con precio 0. ¿Desea continuar?")) return;
    }

    const dto = {
        idPedidoCompra: parseInt(idPedido),
        idProveedor: parseInt(idProv),
        items: items
    };

    try {
        const res = await fetch(`${API_COTIZACION}/registrar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });

        if (res.ok) {
            alert("Cotización registrada exitosamente.");
            // Limpiamos el ID del storage ya que el proceso terminó
            localStorage.removeItem("idPedidoParaCotizar");
            window.location.href = "gestion_pedidos.html";
        } else {
            const errorMsg = await res.text();
            alert("Error del servidor: " + errorMsg);
        }
    } catch (e) {
        alert("Error de conexión con el servidor.");
    }
}