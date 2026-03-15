const API_FACTURAS = "http://localhost:8080/api/facturas/listar";

document.addEventListener("DOMContentLoaded", () => {
    cargarHistorial();

    // Filtro de búsqueda en tiempo real
    document.getElementById("busqueda").addEventListener("keyup", (e) => {
        const texto = e.target.value.toLowerCase();
        const filas = document.querySelectorAll("#tablaHistorial tr");
        filas.forEach(fila => {
            fila.style.display = fila.innerText.toLowerCase().includes(texto) ? "" : "none";
        });
    });
});

async function cargarHistorial() {
    try {
        const res = await fetch(API_FACTURAS);
        if (!res.ok) throw new Error("Error al obtener el historial");

        const facturas = await res.json();
        const tbody = document.getElementById("tablaHistorial");
        tbody.innerHTML = "";

        if (facturas.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-muted">No hay facturas registradas</td></tr>';
            return;
        }

        facturas.forEach(f => {
            // Formatear fecha
            const fecha = new Date(f.fechaRegistro).toLocaleString('es-PE', {
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit'
            });

            tbody.innerHTML += `
                <tr>
                    <td>${fecha}</td>
                    <td><span class="badge bg-info text-dark">${f.ordenCompra.nroOrden}</span></td>
                    <td>${f.ordenCompra.cotizacion.proveedor.razonSocial}</td>
                    <td><strong>${f.serieFactura}-${f.correlativoFactura}</strong></td>
                    <td class="text-end fw-bold text-success">S/ ${f.totalPagado.toFixed(2)}</td>
                    <td class="text-center">
                        <a href="http://localhost:8080${f.pdfUrl}" target="_blank" class="btn btn-outline-danger btn-sm">
                            <i class="fas fa-file-pdf me-1"></i> Ver PDF
                        </a>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error(error);
        alert("No se pudo cargar el historial.");
    }
}