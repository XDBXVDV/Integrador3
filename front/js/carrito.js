document.addEventListener('DOMContentLoaded', () => {
    validarAccesoCarrito();
    renderizarCarrito();
});

// --- LÓGICA DE DATOS LOCALES ---

function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('carrito')) || [];
}

function validarAccesoCarrito() {
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    if (usuario && usuario.rolName.toUpperCase() !== "CLIENTE") {
        const contenedor = document.querySelector('.container');
        if (contenedor) {
            contenedor.innerHTML = `
                <div class="alert alert-warning mt-5 text-center">
                    <h4>Acceso Restringido</h4>
                    <p>Solo los usuarios con rol <strong>CLIENTE</strong> pueden procesar compras.</p>
                    <a href="index.html" class="btn btn-primary">Volver a la tienda</a>
                </div>
            `;
        }
    }
}

window.actualizarResumenPrecios = function() {
    renderizarCarrito();
}

function renderizarCarrito() {
    const carrito = obtenerCarrito();
    const listaContenedor = document.getElementById('lista-carrito');
    const subtotalSpan = document.getElementById('subtotalCart');
    const igvSpan = document.getElementById('igvCart');
    const totalSpan = document.getElementById('totalCart');
    const filaIGV = document.getElementById('filaIGV');
    
    let subtotalGeneral = 0;
    if (!listaContenedor) return;
    listaContenedor.innerHTML = '';

    if (carrito.length === 0) {
        listaContenedor.innerHTML = '<div class="list-group-item text-center py-5">Tu carrito está vacío</div>';
        if(subtotalSpan) subtotalSpan.innerText = '0.00';
        if(totalSpan) totalSpan.innerText = '0.00';
        return;
    }

    carrito.forEach(item => {
        const subtotalItem = item.precio * item.cantidad;
        subtotalGeneral += subtotalItem;
        listaContenedor.innerHTML += `
            <div class="list-group-item d-flex justify-content-between align-items-center p-3">
                <div class="d-flex align-items-center">
                    <img src="${item.imagen}" width="60" class="me-3 rounded shadow-sm">
                    <div>
                        <h6 class="mb-0 fw-bold">${item.nombre}</h6>
                        <small class="text-muted">S/ ${item.precio.toFixed(2)} x ${item.cantidad}</small>
                    </div>
                </div>
                <div class="text-end">
                    <span class="d-block mb-2 fw-bold">S/ ${subtotalItem.toFixed(2)}</span>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-outline-secondary" onclick="actualizarCantidad(${item.idProducto}, -1)">-</button>
                        <button class="btn btn-sm btn-outline-secondary" onclick="actualizarCantidad(${item.idProducto}, 1)">+</button>
                        <button class="btn btn-sm btn-outline-danger ms-2" onclick="eliminarProducto(${item.idProducto})"><i class="fa fa-trash"></i></button>
                    </div>
                </div>
            </div>
        `;
    });

    const tipoDoc = document.querySelector('input[name="tipoDoc"]:checked')?.value || "BOLETA";
    let igv = (tipoDoc === "FACTURA") ? subtotalGeneral * 0.18 : 0;
    let totalFinal = subtotalGeneral + igv;

    if (filaIGV) filaIGV.style.setProperty("display", (tipoDoc === "FACTURA" ? "flex" : "none"), "important");
    if (subtotalSpan) subtotalSpan.innerText = subtotalGeneral.toFixed(2);
    if (igvSpan) igvSpan.innerText = igv.toFixed(2);
    if (totalSpan) totalSpan.innerText = totalFinal.toFixed(2);

    if(typeof actualizarContadorNavbar === 'function') actualizarContadorNavbar();
}

// --- SIMULADOR DE PASARELA MULTI-MÉTODO ---

window.abrirPasarela = function() {
    const total = document.getElementById("totalCart").innerText;
    const nroDoc = document.getElementById("nroDocumento").value;
    const tipoDoc = document.querySelector('input[name="tipoDoc"]:checked')?.value;

    if (obtenerCarrito().length === 0) return alert("El carrito está vacío.");
    if (tipoDoc === "FACTURA" && nroDoc.length !== 11) return alert("Se requiere un RUC válido de 11 dígitos.");
    if (tipoDoc === "BOLETA" && nroDoc.length !== 8) return alert("Se requiere un DNI válido de 8 dígitos.");

    const overlay = document.createElement('div');
    overlay.id = "pago-simulado-overlay";
    overlay.innerHTML = `
        <div style="position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.85); z-index:10000; display:flex; align-items:center; justify-content:center; backdrop-filter: blur(5px);">
            <div class="card shadow" style="width: 400px; border-radius: 15px; overflow: hidden; border: none;">
                <div class="card-header bg-primary text-white text-center py-3">
                    <h5 class="mb-0 text-uppercase fw-bold">Pasarela de Pago Segura</h5>
                </div>
                <div class="card-body p-4" id="cuerpo-pasarela">
                    <p class="text-center text-muted small mb-4">Monto total a debitar: <br> <span class="fs-3 fw-bold text-dark">S/ ${total}</span></p>
                    
                    <div class="d-grid gap-3">
                        <button class="btn btn-outline-primary p-3 text-start" onclick="procesarSimulacion('TARJETA')">
                            <i class="fa fa-credit-card me-2"></i> Tarjeta de Crédito / Débito
                        </button>
                        <button class="btn btn-outline-success p-3 text-start" onclick="procesarSimulacion('YAPE')">
                            <i class="fa fa-mobile-alt me-2"></i> Pagar con Yape
                        </button>
                        <button class="btn btn-outline-info p-3 text-start text-dark" onclick="procesarSimulacion('PLIN')">
                            <i class="fa fa-qrcode me-2"></i> Pagar con Plin
                        </button>
                    </div>
                    
                    <button class="btn btn-link w-100 text-muted mt-4" onclick="document.getElementById('pago-simulado-overlay').remove()">Cancelar Compra</button>
                </div>
                <div id="loader-pasarela" class="card-body p-5 text-center" style="display:none;">
                    <div class="spinner-border text-primary mb-3" style="width: 3rem; height: 3rem;"></div>
                    <h5 class="fw-bold">Procesando...</h5>
                    <p class="text-muted">Validando con la entidad financiera</p>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(overlay);
}

window.procesarSimulacion = function(metodo) {
    document.getElementById("cuerpo-pasarela").style.display = "none";
    document.getElementById("loader-pasarela").style.display = "block";

    setTimeout(() => {
        document.getElementById("pago-simulado-overlay").remove();
        registrarVentaBackend(metodo);
    }, 2000);
}

// --- REGISTRO EN BACKEND ---

async function registrarVentaBackend(metodoConfirmado) {
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    const tipoComprobante = document.querySelector('input[name="tipoDoc"]:checked').value;
    const nroDoc = document.getElementById("nroDocumento").value;
    const subtotal = parseFloat(document.getElementById("subtotalCart").innerText);
    const carrito = obtenerCarrito();

    const ventaDTO = {
        idCliente: parseInt(usuario.idPersona),
        tipoComprobante: tipoComprobante.toUpperCase(),
        nroDocumento: nroDoc,
        metodoPago: metodoConfirmado,
        subtotal: subtotal,
        detalles: carrito.map(item => ({
            idProducto: parseInt(item.idProducto),
            cantidad: parseInt(item.cantidad),
            precioUnitario: parseFloat(item.precio)
        }))
    };

    try {
        const res = await fetch("http://localhost:8080/producto/ventas/registrar", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ventaDTO)
        });

        if (res.ok) {
            const ventaFinal = await res.json();
            alert("¡Pago exitoso! Su pedido ha sido registrado correctamente.");
            generarComprobantePDF(ventaFinal);
            localStorage.removeItem("carrito");
            window.location.href = "index.html";
        } else {
            const errorMsg = await res.text();
            alert("Error del servidor: " + errorMsg);
        }
    } catch (e) {
        alert("Error crítico: No se pudo conectar con el servidor backend (Puerto 8080).");
    }
}

// --- GENERACIÓN DE PDF Y SOPORTE ---

function generarComprobantePDF(v) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.setTextColor(40);
    doc.text("TOISHAN S.A.C.", 105, 20, { align: "center" });
    
    doc.setFontSize(12);
    doc.setTextColor(100);
    doc.text(v.tipoComprobante, 170, 30, { align: "center" });
    doc.text(`N° 001-${String(v.idVenta).padStart(6, '0')}`, 170, 37, { align: "center" });

    doc.setFontSize(10);
    doc.setTextColor(0);
    doc.text(`Cliente: ${v.cliente.nombre} ${v.cliente.apellido}`, 15, 50);
    doc.text(`Documento: ${v.nroDocumento}`, 15, 57);
    doc.text(`Método de Pago: ${v.metodoPago}`, 15, 64);
    doc.text(`Fecha: ${new Date().toLocaleDateString()}`, 15, 71);

    const rows = v.detalles.map(d => [
        d.cantidad, 
        d.producto.nombre, 
        `S/ ${d.precioUnitario.toFixed(2)}`, 
        `S/ ${(d.cantidad * d.precioUnitario).toFixed(2)}`
    ]);

    doc.autoTable({
        startY: 80,
        head: [['Cant', 'Descripción del Producto', 'P. Unitario', 'Subtotal']],
        body: rows,
        theme: 'striped',
        headStyles: { fillColor: [13, 110, 253] }
    });

    const finalY = doc.lastAutoTable.finalY + 10;
    doc.setFontSize(11);
    doc.text(`Subtotal: S/ ${(v.total - v.igv).toFixed(2)}`, 140, finalY);
    if(v.igv > 0) doc.text(`IGV (18%): S/ ${v.igv.toFixed(2)}`, 140, finalY + 7);
    doc.setFontSize(14);
    doc.setFont(undefined, 'bold');
    doc.text(`TOTAL: S/ ${v.total.toFixed(2)}`, 140, finalY + 15);

    doc.save(`${v.tipoComprobante}_${v.idVenta}.pdf`);
}

window.actualizarCantidad = function(id, cambio) {
    let carrito = obtenerCarrito();
    const item = carrito.find(p => p.idProducto === id);
    if (item) {
        const nuevaCant = item.cantidad + cambio;
        if (nuevaCant > item.stock) return alert("Stock máximo alcanzado.");
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