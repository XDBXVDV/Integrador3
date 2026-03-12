const API_VENTAS = "http://localhost:8080/producto/ventas";
let todasLasVentas = []; // Caché local para filtrado rápido
let modalDetalle = null;

document.addEventListener("DOMContentLoaded", async () => {
    modalDetalle = new bootstrap.Modal(document.getElementById('modalDetalleVenta'));
    
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    // Seguridad: Solo empleados o admin
    if (!sesion || sesion.rolName.toUpperCase() === "CLIENTE") {
        alert("No tienes permisos para acceder a este módulo.");
        window.location.href = "acceso_denegado.html";
        return;
    }

    await cargarTodasLasVentas();
});

async function cargarTodasLasVentas() {
    try {
        const res = await fetch(`${API_VENTAS}/listar`); 
        todasLasVentas = await res.json();
        renderizarTabla(todasLasVentas);
    } catch (e) {
        console.error(e);
        document.getElementById("tabla-maestra-ventas").innerHTML = "<tr><td colspan='6' class='text-center text-danger'>Error al conectar con el servidor.</td></tr>";
    }
}

function renderizarTabla(lista) {
    const cuerpo = document.getElementById("tabla-maestra-ventas");
    cuerpo.innerHTML = "";
    document.getElementById("totalVentasBadge").innerText = `${lista.length} Ventas`;

    lista.forEach(v => {
        let fechaMostrar = "Fecha no disponible";
        if (v.fechaVenta) {
            const dateObj = new Date(v.fechaVenta);
            fechaMostrar = isNaN(dateObj) ? v.fechaVenta : dateObj.toLocaleDateString('es-PE');
        }

        let colorEstado = v.estado === "ANULADA" ? "danger" : (v.estado === "COMPLETADA" ? "success" : "primary");

        cuerpo.innerHTML += `
            <tr>
                <td><strong>#${v.idVenta}</strong></td>
                <td>${fechaMostrar}</td>
                <td>${v.nombreCliente}</td>
                <td>S/ ${parseFloat(v.total).toFixed(2)}</td>
                <td><span class="badge bg-${colorEstado}">${v.estado}</span></td>
                <td>
                    <div class="d-flex gap-2">
                        <button class="btn btn-sm btn-info" onclick="verDetalleAdmin(${v.idVenta})">
                            Detalles
                        </button>
                        <button class="btn btn-sm btn-danger" 
                            onclick="confirmarAnulacion(${v.idVenta})" 
                            ${v.estado.toUpperCase() === 'ANULADA' ? 'disabled' : ''}>
                        Anular
                        </button>
                    </div>
                </td>
            </tr>
        `;
    });
}
function filtrarVentas() {
    const texto = document.getElementById("busquedaCliente").value.toLowerCase();
    const estadoSeleccionado = document.getElementById("filtroEstado").value.toUpperCase();
    const desde = document.getElementById("fechaDesde").value;
    const hasta = document.getElementById("fechaHasta").value;

    const filtradas = todasLasVentas.filter(v => {
        // 1. Filtro de Texto (Nombre o ID)
        const nombreCliente = v.nombreCliente ? v.nombreCliente.toLowerCase() : "";
        const idVenta = v.idVenta ? v.idVenta.toString() : "";
        const coincideTexto = nombreCliente.includes(texto) || idVenta.includes(texto);

        // 2. Filtro de Estado
        const estadoVenta = v.estado ? v.estado.toUpperCase() : "";
        const coincideEstado = estadoSeleccionado === "TODOS" || estadoVenta === estadoSeleccionado;

        // 3. Filtro de Fechas
        let coincideFecha = true;
        if (v.fechaVenta) {
            // Convertimos la fecha de la venta (string ISO) a un objeto Date (solo yyyy-mm-dd)
            const fechaVentaCorta = v.fechaVenta.split('T')[0]; 
            
            if (desde && fechaVentaCorta < desde) coincideFecha = false;
            if (hasta && fechaVentaCorta > hasta) coincideFecha = false;
        }

        return coincideTexto && coincideEstado && coincideFecha;
    });

    renderizarTabla(filtradas);
}

// Función para resetear todo rápidamente
window.limpiarFiltros = function() {
    document.getElementById("busquedaCliente").value = "";
    document.getElementById("filtroEstado").value = "TODOS";
    document.getElementById("fechaDesde").value = "";
    document.getElementById("fechaHasta").value = "";
    renderizarTabla(todasLasVentas);
};

window.verDetalleAdmin = async (idVenta) => {
    try {
        const res = await fetch(`${API_VENTAS}/detalle/${idVenta}`);
        const detalles = await res.json();
        
        
        const v = todasLasVentas.find(x => x.idVenta === idVenta);

        document.getElementById("detIdVenta").innerText = idVenta;
        document.getElementById("detCliente").innerText = v.nombreCliente;
        document.getElementById("detMetodo").innerText = v.metodoPago || "No especificado";
        document.getElementById("detTotal").innerText = v.total.toFixed(2);

        const cuerpo = document.getElementById("detCuerpoTabla");
        cuerpo.innerHTML = "";
        detalles.forEach(d => {
            cuerpo.innerHTML += `
                <tr>
                    <td>${d.nombreProducto}</td>
                    <td>S/ ${d.precioUnitario.toFixed(2)}</td>
                    <td>${d.cantidad}</td>
                    <td>S/ ${d.subtotal.toFixed(2)}</td>
                </tr>
            `;
        });

        modalDetalle.show();
    } catch (e) {
        alert("Error al cargar el detalle.");
    }
};


async function confirmarAnulacion(idVenta) {
    try {
        const response = await fetch(`${API_VENTAS}/anular/${idVenta}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            alert(`¡Venta #${idVenta} anulada correctamente!`);
            
            // ACTUALIZACIÓN LOCAL: 
            // Buscamos la venta en el array 'todasLasVentas' y cambiamos su estado
            const index = todasLasVentas.findIndex(v => v.idVenta === idVenta);
            if (index !== -1) {
                todasLasVentas[index].estado = "ANULADA";
                
                // Volvemos a renderizar la tabla con los datos actualizados
                // Esto hará que el botón de anular se deshabilite y el badge cambie a rojo
                renderizarTabla(todasLasVentas);
            }
        } else {
            const errorMsg = await response.text();
            alert("No se pudo anular la venta: " + errorMsg);
        }
    } catch (error) {
        console.error("Error al intentar anular:", error);
        alert("Ocurrió un error de conexión con el servidor.");
    }
}

function imprimirReporte() {
    window.print(); 
}