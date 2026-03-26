const API_URL = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {
    const hoy = new Date();
    const haceUnMes = new Date();
    haceUnMes.setMonth(hoy.getMonth() - 1);

    document.getElementById("fechaFin").value = hoy.toISOString().split('T')[0];
    document.getElementById("fechaInicio").value = haceUnMes.toISOString().split('T')[0];
    
    consultarMovimientos();
});

async function consultarMovimientos() {
    const inicio = document.getElementById("fechaInicio").value;
    const fin = document.getElementById("fechaFin").value;
    const tipo = document.getElementById("filtTipo").value;

    const res = await fetch(`${API_URL}/guias/reporte?inicio=${inicio}&fin=${fin}&tipo=${tipo}`);
    const movimientos = await res.json();
    
    const tbody = document.getElementById("tablaReporte");
    tbody.innerHTML = "";

    movimientos.forEach(m => {
        const color = m.tipoMovimiento === 'ENTRADA' ? 'text-success' : 'text-danger';
        const icon = m.tipoMovimiento === 'ENTRADA' ? 'fa-arrow-down' : 'fa-arrow-up';

        tbody.innerHTML += `
            <tr>
                <td>${new Date(m.fechaMovimiento).toLocaleString()}</td>
                <td><strong>${m.numeroGuia}</strong></td>
                <td class="${color} fw-bold">
                    <i class="fas ${icon} me-1"></i> ${m.tipoMovimiento}
                </td>
                <td>${m.motivo}</td>
                <td>${m.empleadoAlmacen.nombre}</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-danger" onclick="descargarGuiaPdf(${m.idGuia})">
                        <i class="fas fa-file-pdf">Ver PDF</i>
                    </button>
                </td>
            </tr>`;
    });
}

function descargarGuiaPdf(id) {
    window.open(`${API_URL}/guias/pdf/${id}`, '_blank');
}

function descargarReporteConsolidado() {
    const inicio = document.getElementById("fechaInicio").value;
    const fin = document.getElementById("fechaFin").value;
    const tipo = document.getElementById("filtTipo").value;

    if (!inicio || !fin) {
        return alert("Por favor seleccione un rango de fechas válido.");
    }

    // Construimos la URL con los parámetros actuales
    const url = `${API_URL}/guias/reporte-pdf-consolidado?inicio=${inicio}&fin=${fin}&tipo=${tipo}`;
    
    // Abrimos el PDF en una pestaña nueva
    window.open(url, '_blank');
}