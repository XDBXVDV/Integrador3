const API_BASE = "http://localhost:8080/api/pedidos-compra";
let pedidosCache = [];

document.addEventListener("DOMContentLoaded", () => {
    cargarPedidos();
});

async function cargarPedidos() {
    try {
        const res = await fetch(`${API_BASE}/todos`); 
        
        if (!res.ok) throw new Error("Error al obtener datos");
        
        pedidosCache = await res.json();
        const tbody = document.getElementById("tablaPedidos");
        tbody.innerHTML = "";

        if (pedidosCache.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center py-4 text-muted">No hay requerimientos pendientes</td></tr>';
            return;
        }

        pedidosCache.forEach(p => {
            const estadoMayus = p.estado?.toUpperCase();
            
            const badgeColor = {
                'PENDIENTE': 'bg-warning text-dark',
                'COTIZADO': 'bg-info text-white',
                'ANULADO': 'bg-danger'
            }[estadoMayus] || 'bg-secondary';

            tbody.innerHTML += `
                <tr>
                    <td><strong>#${p.idPedidoCompra}</strong></td>
                    <td>${p.fecha ? new Date(p.fecha).toLocaleString() : '---'}</td>
                    <td>${p.empleado?.nombre || 'N/A'} ${p.empleado?.apellido || ''}</td>
                    <td><span class="badge ${badgeColor}">${p.estado}</span></td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-info text-white" onclick="verDetalle(${p.idPedidoCompra})" title="Ver detalle">
                            <i class="fas fa-eye">Ver detalle</i>
                        </button>
                        
                        ${estadoMayus === 'PENDIENTE' ? `
                            <button class="btn btn-sm btn-primary" onclick="irACotizar(${p.idPedidoCompra})" title="Generar Cotización">
                                <i class="fas fa-file-invoice-dollar"></i> Cotizar
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="anularRequerimiento(${p.idPedidoCompra})" title="Anular">
                                <i class="fas fa-times"></i>Anular
                            </button>
                        ` : ''}
                    </td>
                </tr>
            `;
        });
    } catch (e) {
        console.error("Error:", e);
    }
}

window.verDetalle = function(id) {
    const p = pedidosCache.find(x => x.idPedidoCompra === id);
    if (!p) return;

    document.getElementById("detIdPedido").innerText = p.idPedidoCompra;
    document.getElementById("infoPedido").innerHTML = `
        <strong>Solicitante:</strong> ${p.empleado?.nombre} ${p.empleado?.apellido}<br>
        <strong>Fecha:</strong> ${new Date(p.fecha).toLocaleString()} | <strong>Estado:</strong> ${p.estado}
    `;

    const cuerpo = document.getElementById("cuerpoDetalle");
    cuerpo.innerHTML = "";
    
    p.detalles.forEach(d => {
        cuerpo.innerHTML += `
            <tr>
                <td>${d.producto?.nombre || 'Producto no identificado'}</td>
                <td class="text-center fw-bold">${d.cantidad}</td>
            </tr>
        `;
    });

    new bootstrap.Modal(document.getElementById('modalDetalle')).show();
}

window.irACotizar = function(id) {
    // Guardamos el ID en el almacenamiento local para que la pantalla de cotización sepa qué cargar
    localStorage.setItem("idPedidoParaCotizar", id);
    window.location.href = "generar_cotizacion.html";
}

window.anularRequerimiento = async function(id) {
    if (!confirm("¿Desea anular este requerimiento de compra?")) return;

    try {
        const res = await fetch(`${API_BASE}/${id}/estado?nuevoEstado=ANULADO`, {
            method: 'PUT'
        });

        if (res.ok) {
            alert("Requerimiento anulado.");
            cargarPedidos();
        }
    } catch (e) {
        alert("Error de conexión");
    }
}