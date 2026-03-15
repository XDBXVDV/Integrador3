const API_COT = "http://localhost:8080/api/cotizaciones";

document.addEventListener("DOMContentLoaded", () => {
    listarCotizacionesPendientes();
});

async function listarCotizacionesPendientes() {
    try {
        const res = await fetch(`${API_COT}/pendientes`);
        
        if (!res.ok) {
            throw new Error("No se encontraron cotizaciones o la ruta es incorrecta");
        }

        const lista = await res.json();
        const tbody = document.getElementById("tablaAprobaciones");
        tbody.innerHTML = "";

        if (lista.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No hay cotizaciones pendientes de aprobación</td></tr>';
            return;
        }

        lista.forEach(c => {
            tbody.innerHTML += `
                <tr>
                    <td><strong>#${c.idCotizacion}</strong></td>
                    <td>Pedido #${c.pedidoCompra?.idPedidoCompra || 'N/A'}</td>
                    <td>${c.proveedor?.razonSocial || 'N/A'}</td>
                    <td class="fw-bold text-primary">S/ ${c.totalCotizado.toFixed(2)}</td>
                    <td class="text-center">
                        <button class="btn btn-success btn-sm me-2" onclick="procesarAprobacion(${c.idCotizacion}, 'aprobar')">
                            <i class="fas fa-check"></i> Aprobar
                        </button>
                        <button class="btn btn-danger btn-sm" onclick="procesarAprobacion(${c.idCotizacion}, 'rechazar')">
                            <i class="fas fa-times"></i> Rechazar
                        </button>
                    </td>
                </tr>
            `;
        });
    } catch (e) {
        console.error("Error:", e);
        alert("Error al cargar la tabla: " + e.message);
    }
}

async function procesarAprobacion(id, accion) {
    if (!confirm(`¿Está seguro de ${accion} esta cotización?`)) return;

    try {
        const res = await fetch(`${API_COT}/${id}/${accion}`, { method: 'PUT' });
        if (res.ok) {
            alert(`Cotización ${accion === 'aprobar' ? 'aceptada y Orden de Compra generada' : 'rechazada'}.`);
            listarCotizacionesPendientes();
        }
    } catch (e) {
        alert("Error al procesar.");
    }
}