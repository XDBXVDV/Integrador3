const API_FACTURAS = "http://localhost:8080/api/facturas/listar";
const API_REPORTES = "http://localhost:8080/api/reportes/compras";

let miGrafico = null; // Variable global para el gráfico

document.addEventListener("DOMContentLoaded", () => {
    // Establecer fechas por defecto (mes actual)
    const hoy = new Date();
    const primerDia = new Date(hoy.getFullYear(), hoy.getMonth(), 1).toISOString().split('T')[0];
    const ultimoDia = hoy.toISOString().split('T')[0];
    
    document.getElementById("fechaInicio").value = primerDia;
    document.getElementById("fechaFin").value = ultimoDia;

    actualizarDashboard();
});

async function actualizarDashboard() {
    try {
        const res = await fetch(API_FACTURAS);
        const facturas = await res.json();

        procesarGrafico(facturas);
        actualizarResumen(facturas);
    } catch (error) {
        console.error("Error al cargar dashboard:", error);
    }
}

function procesarGrafico(facturas) {
    // Agrupar totales por proveedor
    const totalesPorProv = {};
    facturas.forEach(f => {
        const prov = f.ordenCompra.cotizacion.proveedor.razonSocial;
        totalesPorProv[prov] = (totalesPorProv[prov] || 0) + f.totalPagado;
    });

    const labels = Object.keys(totalesPorProv);
    const valores = Object.values(totalesPorProv);

    const ctx = document.getElementById('graficoProveedores').getContext('2d');

    // Si ya existe un gráfico, lo destruimos para crear el nuevo
    if (miGrafico) miGrafico.destroy();

    miGrafico = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Suma de Facturas (S/)',
                data: valores,
                backgroundColor: 'rgba(13, 110, 253, 0.7)',
                borderColor: 'rgba(13, 110, 253, 1)',
                borderWidth: 1,
                borderRadius: 5
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });
}

function actualizarResumen(facturas) {
    // Calcular Total General
    const total = facturas.reduce((acc, f) => acc + f.totalPagado, 0);
    document.getElementById("totalGeneral").innerText = `S/ ${total.toFixed(2)}`;

    // Mostrar últimas 5 facturas
    const lista = document.getElementById("listaUltimosMovimientos");
    lista.innerHTML = "";
    
    facturas.slice(-5).reverse().forEach(f => {
        lista.innerHTML += `
            <div class="list-group-item">
                <div class="d-flex justify-content-between">
                    <small class="text-muted">${f.serieFactura}-${f.correlativoFactura}</small>
                    <small class="fw-bold text-success">S/ ${f.totalPagado.toFixed(2)}</small>
                </div>
                <div class="text-truncate" style="max-width: 200px;">${f.ordenCompra.cotizacion.proveedor.razonSocial}</div>
            </div>`;
    });
}

function descargarReportePDF() {
    const inicio = document.getElementById("fechaInicio").value;
    const fin = document.getElementById("fechaFin").value;

    if (!inicio || !fin) {
        alert("Seleccione un rango de fechas válido.");
        return;
    }

    // URL con parámetros para el controlador de Spring Boot
    const url = `${API_REPORTES}/exportar/pdf?inicio=${inicio}&fin=${fin}`;
    
    // Abrir en pestaña nueva para disparar la descarga
    window.open(url, '_blank');
}