const API_REPORTES = "http://localhost:8080/producto/ventas/reporte";
let chartVentas = null;
let chartTop = null;
let datosFiltradosParaPDF = []; // Variable global para sincronizar con el PDF

document.addEventListener("DOMContentLoaded", () => {
    // 1. Verificar seguridad (solo empleados/admin)
    if (typeof verificarEmpleado === "function") {
        verificarEmpleado();
    }

    // 2. Carga inicial de datos
    cargarReportes();
});

/**
 * Carga los datos de los endpoints y aplica filtros de fecha si existen
 */
async function cargarReportes() {
    const fechaDesde = document.getElementById("repDesde").value;
    const fechaHasta = document.getElementById("repHasta").value;

    try {
        // Peticiones en paralelo para mayor velocidad
        const [resDiario, resTop] = await Promise.all([
            fetch(`${API_REPORTES}/diario`),
            fetch(`${API_REPORTES}/top-productos`)
        ]);

        let datosDiario = await resDiario.json();
        const datosTop = await resTop.json();

        // --- LÓGICA DE FILTRADO ---
        if (fechaDesde || fechaHasta) {
            datosDiario = datosDiario.filter(d => {
                // Normalizamos la fecha (YYYY-MM-DD)
                const fechaItem = d.etiqueta.split('T')[0].split(' ')[0];
                
                let cumpleDesde = true;
                let cumpleHasta = true;

                if (fechaDesde) cumpleDesde = (fechaItem >= fechaDesde);
                if (fechaHasta) cumpleHasta = (fechaItem <= fechaHasta);

                return cumpleDesde && cumpleHasta;
            });
        }

        // Guardamos en la global para el PDF
        datosFiltradosParaPDF = datosDiario;

        // --- RENDERIZADO ---
        renderizarGraficoVentas(datosDiario);
        renderizarGraficoTop(datosTop);
        actualizarTarjetasResumen(datosDiario);

    } catch (error) {
        console.error("Error al cargar reportes:", error);
        alert("Error de conexión con el servidor de reportes.");
    }
}

/**
 * Genera o actualiza el gráfico de líneas de ventas diarias
 */
function renderizarGraficoVentas(datos) {
    const ctx = document.getElementById('chartVentas').getContext('2d');
    
    if (chartVentas) chartVentas.destroy();

    chartVentas = new Chart(ctx, {
        type: 'line',
        data: {
            labels: datos.map(d => d.etiqueta.split('T')[0]),
            datasets: [{
                label: 'Ingresos Diarios (S/)',
                data: datos.map(d => d.valor),
                borderColor: '#0d6efd',
                backgroundColor: 'rgba(13, 110, 253, 0.1)',
                borderWidth: 3,
                fill: true,
                tension: 0.3,
                pointRadius: 4,
                pointBackgroundColor: '#0d6efd'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}

/**
 * Genera o actualiza el gráfico de dona de productos más vendidos
 */
function renderizarGraficoTop(datos) {
    const ctx = document.getElementById('chartTop').getContext('2d');
    
    if (chartTop) chartTop.destroy();

    chartTop = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: datos.map(d => d.etiqueta),
            datasets: [{
                data: datos.map(d => d.valor),
                backgroundColor: ['#0d6efd', '#198754', '#ffc107', '#dc3545', '#6610f2'],
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
}

/**
 * Actualiza los números de las tarjetas superiores
 */
function actualizarTarjetasResumen(datos) {
    const total = datos.reduce((acc, curr) => acc + curr.valor, 0);
    const numVentas = datos.length;
    const ticketPromedio = numVentas > 0 ? (total / numVentas) : 0;

    document.getElementById("resumenTotal").innerText = `S/ ${total.toFixed(2)}`;
    document.getElementById("resumenOrdenes").innerText = numVentas;
    document.getElementById("resumenTicket").innerText = `S/ ${ticketPromedio.toFixed(2)}`;
}

/**
 * Genera el archivo PDF con una tabla detallada de lo filtrado
 */
window.exportarPDF = function() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // Configuración inicial del PDF
    doc.setFillColor(13, 110, 253);
    doc.rect(0, 0, 210, 35, 'F');
    
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(20);
    doc.text("TOISHAN - REPORTE DE VENTAS", 15, 22);
    
    doc.setFontSize(10);
    doc.text(`Generado el: ${new Date().toLocaleString()}`, 15, 30);

    doc.setTextColor(40, 40, 40);
    doc.setFontSize(12);
    doc.text("Resumen de Periodo Seleccionado", 15, 45);

    // Verificamos si hay datos para la tabla
    if (datosFiltradosParaPDF && datosFiltradosParaPDF.length > 0) {
        
        // Formatear datos para AutoTable: [[col1, col2]]
        const filas = datosFiltradosParaPDF.map(item => [
            item.etiqueta.split('T')[0],
            `S/ ${item.valor.toFixed(2)}`
        ]);

        doc.autoTable({
            startY: 50,
            head: [['Fecha de Operación', 'Monto Total Vendido']],
            body: filas,
            theme: 'striped',
            headStyles: { fillColor: [13, 110, 253] },
            styles: { fontSize: 10, cellPadding: 3 }
        });

        // Monto total al final
        const totalFinal = datosFiltradosParaPDF.reduce((acc, d) => acc + d.valor, 0);
        const finalY = doc.lastAutoTable.finalY;
        
        doc.setFontSize(12);
        doc.setFont(undefined, 'bold');
        doc.text(`TOTAL DEL PERIODO: S/ ${totalFinal.toFixed(2)}`, 140, finalY + 15, { align: 'right' });

    } else {
        doc.setTextColor(220, 53, 69);
        doc.text("No se encontraron registros para el rango de fechas actual.", 15, 55);
    }

    doc.save(`Reporte_Ventas_${new Date().getTime()}.pdf`);
};

/**
 * Función opcional para limpiar filtros
 */
window.limpiarFiltros = function() {
    document.getElementById("repDesde").value = "";
    document.getElementById("repHasta").value = "";
    cargarReportes();
};