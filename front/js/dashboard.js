const API_URL = "http://localhost:8080/api/dashboard";

document.addEventListener("DOMContentLoaded", () => {
    cargarDatosDashboard();
});

async function cargarDatosDashboard() {
    try {
        const res = await fetch(`${API_URL}/resumen`);
        const data = await res.json();

        // 1. Inyectar KPIs (Con formato de moneda)
        document.getElementById("kpiVentas").innerText = new Intl.NumberFormat('es-PE', { style: 'currency', currency: 'PEN' }).format(data.totalVentasMes);
        document.getElementById("kpiCompras").innerText = new Intl.NumberFormat('es-PE', { style: 'currency', currency: 'PEN' }).format(data.totalComprasMes);
        document.getElementById("kpiCriticos").innerText = data.productosCriticos;

        // 2. Preparar datos para el gráfico
        // data.etiquetasDias viene como ["Monday", "Tuesday"...]
        // data.datosVentas viene como [150.0, 300.5...]

        const ctx = document.getElementById('chartVentasSemanales').getContext('2d');
        
        // Destruir gráfico previo si existiera (buena práctica)
        if (window.myChart) window.myChart.destroy();

        window.myChart = new Chart(ctx, {
            type: 'bar', // Puedes cambiar a 'line' si prefieres curvas
            data: {
                labels: data.etiquetasDias, // Nombres de los días de tu SQL
                datasets: [{
                    label: 'Venta Diaria (S/)',
                    data: data.datosVentas, // Montos de tu SQL
                    backgroundColor: 'rgba(13, 110, 253, 0.7)',
                    borderColor: '#0d6efd',
                    borderWidth: 2,
                    borderRadius: 5
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) { return 'S/ ' + value; }
                        }
                    }
                }
            }
        });

    } catch (error) {
        console.error("Error cargando el Dashboard:", error);
    }
}