const API_URL = "http://localhost:8080/api/dashboard";

document.addEventListener("DOMContentLoaded", () => {
  
    cargarDatosDashboard();
});

async function cargarDatosDashboard() {
    

    try {
        const res = await fetch(`${API_URL}/resumen`, {
            method: "GET",
        });

        

        const data = await res.json();

        // 1. Actualizar KPIs
        document.getElementById("kpiVentas").innerText = `S/ ${data.totalVentasMes.toFixed(2)}`;
        document.getElementById("kpiCompras").innerText = `S/ ${data.totalComprasMes.toFixed(2)}`;
        document.getElementById("kpiCriticos").innerText = data.productosCriticos;

        // 2. Gráfico Lineal: Ventas vs Compras
        const ctxVentas = document.getElementById('chartVentasSemanales').getContext('2d');
        if (window.myChartVentas) window.myChartVentas.destroy();

        window.myChartVentas = new Chart(ctxVentas, {
            type: 'line',
            data: {
                labels: data.etiquetasDias,
                datasets: [
                    {
                        label: 'Ventas (S/)',
                        data: data.datosVentas,
                        borderColor: '#0d6efd',
                        backgroundColor: 'rgba(13, 110, 253, 0.1)',
                        fill: true,
                        tension: 0.4
                    },
                    {
                        label: 'Compras (S/)',
                        data: data.datosCompras,
                        borderColor: '#198754',
                        borderDash: [5, 5],
                        fill: false,
                        tension: 0.4
                    }
                ]
            },
            options: { 
                responsive: true, 
                maintainAspectRatio: false,
                scales: { y: { beginAtZero: true } }
            }
        });

        // 3. Gráfico Doughnut: Categorías
        const ctxCat = document.getElementById('chartCategorias').getContext('2d');
        if (window.myChartCat) window.myChartCat.destroy();

        window.myChartCat = new Chart(ctxCat, {
            type: 'doughnut',
            data: {
                labels: data.etiquetasCat,
                datasets: [{
                    data: data.datosCat,
                    backgroundColor: ['#0d6efd', '#198754', '#ffc107', '#dc3545', '#6610f2']
                }]
            },
            options: { 
                responsive: true, 
                maintainAspectRatio: false,
                plugins: { legend: { position: 'bottom' } }
            }
        });

        // 4. Tabla Top Productos
        const tbody = document.getElementById("tablaTopProductos");
        tbody.innerHTML = "";
        data.topProductos.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td class="ps-4 fw-bold">${p.nombre}</td>
                    <td class="text-center">${p.cantidad}</td>
                    <td class="text-end pe-4 fw-bold text-primary">S/ ${p.total.toFixed(2)}</td>
                </tr>`;
        });

    } catch (error) {
        console.error("Error cargando el dashboard:", error);
    }
}

function cerrarSesion() {
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
    window.location.href = "login.html";
}