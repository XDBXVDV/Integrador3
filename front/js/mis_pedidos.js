const API_VENTAS = "http://localhost:8080/producto/ventas";
let modalDetalle = null;

document.addEventListener("DOMContentLoaded", () => {
    modalDetalle = new bootstrap.Modal(document.getElementById('modalDetallePedido'));
    
    const sesion = JSON.parse(localStorage.getItem("usuario"));
    if (!sesion || sesion.rolName.toUpperCase() !== "CLIENTE") {
        window.location.href = "index.html";
        return;
    }

    cargarHistorialPedidos(sesion.idPersona);
});

async function cargarHistorialPedidos(idCliente) {
    const tabla = document.getElementById("lista-pedidos");
    try {
        const res = await fetch(`${API_VENTAS}/cliente/${idCliente}`);
        const pedidos = await res.json();
        tabla.innerHTML = "";

        pedidos.forEach(p => {
            const fecha = new Date(p.fechaventa).toLocaleDateString();
            tabla.innerHTML += `
                <tr>
                    <td><strong>#${p.idVenta}</strong></td>
                    <td>${fecha}</td>
                    <td><small>${p.tipoComprobante}: ${p.nroDocumento}</small></td>
                    <td>S/ ${p.total.toFixed(2)}</td>
                    <td><span class="badge bg-success">${p.estado}</span></td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-info" onclick="verDetallePedido(${p.idVenta}, ${p.total})">
                            <i class="fa fa-eye"></i> Detalle
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="reimprimirPDF(${p.idVenta})">
                            <i class="fa fa-file-pdf"></i> PDF
                        </button>
                    </td>
                </tr>
            `;
        });
    } catch (e) { console.error(e); }
}

async function reimprimirPDF(idVenta) {
    try {
        const res = await fetch(`${API_VENTAS}/buscar/${idVenta}`);
        if (!res.ok) throw new Error("No se pudo obtener la información de la venta");
        const v = await res.json();
        
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        // Encabezado
        doc.setFontSize(16);
        doc.text("TOISHAN S.A.C.", 105, 15, { align: "center" });
        doc.setFontSize(12);
        doc.text(v.tipoComprobante, 170, 25, { align: "center" });
        doc.text(`N° 001-${String(v.idVenta).padStart(6, '0')}`, 170, 32, { align: "center" });

        // Datos del cliente (v viene con el objeto cliente del backend)
        doc.setFontSize(10);
        doc.text(`Cliente: ${v.cliente.nombre} ${v.cliente.apellido}`, 15, 45);
        doc.text(`Documento: ${v.nroDocumento}`, 15, 52);
        doc.text(`Fecha de compra: ${new Date(v.fechaventa).toLocaleDateString()}`, 15, 59);

        // Tabla de productos
        const filas = v.detalles.map(d => [
            d.cantidad, 
            d.producto.nombre, 
            `S/ ${d.precioUnitario.toFixed(2)}`, 
            `S/ ${(d.cantidad * d.precioUnitario).toFixed(2)}`
        ]);

        doc.autoTable({
            startY: 65,
            head: [['Cant', 'Producto', 'P. Unit', 'Subtotal']],
            body: filas,
            theme: 'grid',
            headStyles: { fillColor: [40, 40, 40] }
        });

        // Totales
        const finalY = doc.lastAutoTable.finalY + 10;
        const subtotal = v.total - v.igv;
        doc.text(`Subtotal: S/ ${subtotal.toFixed(2)}`, 140, finalY);
        if(v.igv > 0) doc.text(`IGV (18%): S/ ${v.igv.toFixed(2)}`, 140, finalY + 7);
        doc.setFontSize(12); doc.setFont(undefined, 'bold');
        doc.text(`TOTAL: S/ ${v.total.toFixed(2)}`, 140, finalY + 15);

        doc.save(`${v.tipoComprobante}_${v.idVenta}.pdf`);
    } catch (e) {
        console.error(e);
        alert("Error al recuperar el PDF: " + e.message);
    }
}
window.verDetallePedido = async (idVenta, total) => {
    try {
        const res = await fetch(`${API_VENTAS}/detalle/${idVenta}`);
        const detalles = await res.json();
        const tablaDetalle = document.getElementById("lista-detalle-pedido");
        document.getElementById("numPedido").innerText = idVenta;
        document.getElementById("totalPedidoModal").innerText = total.toFixed(2);
        
        tablaDetalle.innerHTML = detalles.map(d => `
            <tr>
                <td>${d.nombreProducto}</td>
                <td>S/ ${d.precioUnitario.toFixed(2)}</td>
                <td>${d.cantidad}</td>
                <td>S/ ${d.subtotal.toFixed(2)}</td>
            </tr>
        `).join("");
        
        modalDetalle.show();
    } catch (e) { alert("Error al cargar detalle"); }
};