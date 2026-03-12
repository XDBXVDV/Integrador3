document.addEventListener('DOMContentLoaded', () => {
    validarAccesoCarrito();
    renderizarCarrito();
});

function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('carrito')) || [];
}

function validarAccesoCarrito() {
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    if (usuario && usuario.rolName.toUpperCase() !== "CLIENTE") {
        const contenedor = document.querySelector('.container');
        contenedor.innerHTML = `
            <div class="alert alert-warning mt-5 text-center">
                <h4>Solo clientes</h4>
                <p>Lo sentimos, las cuentas de <strong>${usuario.rolName}</strong> no pueden realizar compras.</p>
                <a href="index.html" class="btn btn-primary">Volver a la tienda</a>
            </div>
        `;
    }
}

// Esta función se llama al cambiar de Boleta a Factura en el HTML
window.actualizarResumenPrecios = function() {
    renderizarCarrito();
}

function renderizarCarrito() {
    const carrito = obtenerCarrito();
    const listaContenedor = document.getElementById('lista-carrito');
    
    // IDs de tu HTML actual
    const subtotalSpan = document.getElementById('subtotalCart');
    const igvSpan = document.getElementById('igvCart');
    const totalSpan = document.getElementById('totalCart');
    const filaIGV = document.getElementById('filaIGV');
    
    let subtotalGeneral = 0;
    listaContenedor.innerHTML = '';

    if (carrito.length === 0) {
        listaContenedor.innerHTML = '<div class="list-group-item text-center">El carrito está vacío</div>';
        if(subtotalSpan) subtotalSpan.innerText = '0.00';
        if(totalSpan) totalSpan.innerText = '0.00';
        return;
    }

    carrito.forEach(item => {
        const subtotalItem = item.precio * item.cantidad;
        subtotalGeneral += subtotalItem;

        listaContenedor.innerHTML += `
            <div class="list-group-item d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center">
                    <img src="${item.imagen}" width="50" class="me-3 rounded">
                    <div>
                        <h6 class="mb-0">${item.nombre}</h6>
                        <small class="text-muted">S/ ${item.precio.toFixed(2)} x ${item.cantidad}</small>
                    </div>
                </div>
                <div>
                    <span class="me-3 fw-bold">S/ ${subtotalItem.toFixed(2)}</span>
                    <button class="btn btn-sm btn-outline-secondary" onclick="actualizarCantidad(${item.idProducto}, -1)">-</button>
                    <button class="btn btn-sm btn-outline-secondary" onclick="actualizarCantidad(${item.idProducto}, 1)">+</button>
                    <button class="btn btn-sm btn-danger ms-2" onclick="eliminarProducto(${item.idProducto})">×</button>
                </div>
            </div>
        `;
    });

    // Lógica de Impuestos (IGV)
    const tipoDoc = document.querySelector('input[name="tipoDoc"]:checked')?.value || "BOLETA";
    let igv = 0;
    let totalFinal = subtotalGeneral;

    if (tipoDoc === "FACTURA") {
        igv = subtotalGeneral * 0.18;
        totalFinal = subtotalGeneral + igv;
        filaIGV.style.setProperty("display", "flex", "important");
    } else {
        filaIGV.style.setProperty("display", "none", "important");
    }

    subtotalSpan.innerText = subtotalGeneral.toFixed(2);
    igvSpan.innerText = igv.toFixed(2);
    totalSpan.innerText = totalFinal.toFixed(2);

    if(typeof actualizarContadorNavbar === 'function') actualizarContadorNavbar();
}


window.realizarPedido = async function() {
    const carrito = obtenerCarrito();
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    
  
    const tipoComprobante = document.querySelector('input[name="tipoDoc"]:checked').value; 
    const nroDoc = document.getElementById("nroDocumento").value;
    const subtotalText = document.getElementById("subtotalCart").innerText;

    if (carrito.length === 0) return alert("Tu carrito está vacío.");
    if (!usuario) { alert("Debes iniciar sesión."); return; }

  
    if (tipoComprobante === "FACTURA" && nroDoc.length !== 11) return alert("RUC inválido (11 dígitos)");
    if (tipoComprobante === "BOLETA" && nroDoc.length !== 8) return alert("DNI inválido (8 dígitos)");

    const ventaDTO = {
        idCliente: parseInt(usuario.idPersona), 
        tipoComprobante: tipoComprobante.toUpperCase(), 
        nroDocumento: nroDoc,
        metodoPago: "TARJETA", 
        subtotal: parseFloat(subtotalText),
        detalles: carrito.map(item => ({
            idProducto: parseInt(item.idProducto),
            cantidad: parseInt(item.cantidad),
            precioUnitario: parseFloat(item.precio)
        }))
    };

    console.log("Enviando al servidor:", ventaDTO); 

    try {
        const res = await fetch("http://localhost:8080/producto/ventas/registrar", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ventaDTO)
        });

        if (res.ok) {
            const ventaFinal = await res.json();
            alert("¡Compra exitosa!");
            generarComprobantePDF(ventaFinal);
            localStorage.removeItem("carrito");
            setTimeout(() => { window.location.href = "index.html"; }, 2000);
        } else {
            // Si el error es 400, aquí capturamos el porqué
            const errorTexto = await res.text();
            console.error("Error 400 detallado:", errorTexto);
            alert("Error en los datos: " + errorTexto);
        }
    } catch (e) { 
        console.error("Error de conexión:", e); 
    }
}

function generarComprobantePDF(v) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.setFontSize(16);
    doc.text("TOISHAN S.A.C.", 105, 15, { align: "center" });
    doc.setFontSize(12);
    doc.text(v.tipoComprobante, 170, 25, { align: "center" });
    doc.text(`N° 001-${String(v.idVenta).padStart(6, '0')}`, 170, 32, { align: "center" });

    doc.setFontSize(10);
    doc.text(`Cliente ID: ${v.cliente.idPersona}`, 15, 45);
    doc.text(`Documento: ${v.nroDocumento}`, 15, 52);
    doc.text(`Fecha: ${new Date().toLocaleDateString()}`, 15, 59);

    const rows = v.detalles.map(d => [
        d.cantidad, 
        d.producto.nombre, 
        `S/ ${d.precioUnitario.toFixed(2)}`, 
        `S/ ${(d.cantidad * d.precioUnitario).toFixed(2)}`
    ]);

    doc.autoTable({
        startY: 65,
        head: [['Cant', 'Producto', 'P. Unit', 'Subtotal']],
        body: rows,
        theme: 'grid'
    });

    const finalY = doc.lastAutoTable.finalY + 10;
    doc.text(`SUBTOTAL: S/ ${(v.total - v.igv).toFixed(2)}`, 140, finalY);
    if(v.igv > 0) doc.text(`IGV (18%): S/ ${v.igv.toFixed(2)}`, 140, finalY + 7);
    doc.setFontSize(12);
    doc.text(`TOTAL: S/ ${v.total.toFixed(2)}`, 140, finalY + 15);

    doc.save(`${v.tipoComprobante}_${v.idVenta}.pdf`);
}

window.actualizarCantidad = function(id, cambio) {
    let carrito = obtenerCarrito();
    const item = carrito.find(p => p.idProducto === id);
    if (item) {
        const nuevaCant = item.cantidad + cambio;
        if (nuevaCant > item.stock) return alert("Sin stock suficiente");
        if (nuevaCant <= 0) return eliminarProducto(id);
        item.cantidad = nuevaCant;
        localStorage.setItem('carrito', JSON.stringify(carrito));
        renderizarCarrito();
    }
};

window.eliminarProducto = function(id) {
    let carrito = obtenerCarrito().filter(p => p.idProducto !== id);
    localStorage.setItem('carrito', JSON.stringify(carrito));
    renderizarCarrito();
};