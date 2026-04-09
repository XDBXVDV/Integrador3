
Culqi.publicKey = 'pk_test_LB4RPCTbzBp2ewa7'; 
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
        if (contenedor) {
            contenedor.innerHTML = `
                <div class="alert alert-warning mt-5 text-center shadow-sm">
                    <h4 class="fw-bold"><i class="fas fa-exclamation-triangle me-2"></i>Acceso Restringido</h4>
                    <p>Solo los usuarios con rol <strong>CLIENTE</strong> pueden procesar compras.</p>
                    <a href="index.html" class="btn btn-primary mt-2">Volver a la tienda</a>
                </div>`;
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
        listaContenedor.innerHTML = '<div class="list-group-item text-center py-5 text-muted">Tu carrito está vacío</div>';
        if(subtotalSpan) subtotalSpan.innerText = '0.00';
        if(totalSpan) totalSpan.innerText = '0.00';
        return;
    }

    carrito.forEach(item => {
        const subtotalItem = item.precio * item.cantidad;
        subtotalGeneral += subtotalItem;

        // Limpieza de ruta para imágenes (Compatibilidad con ngrok)
        const nombreArchivo = item.imagen.split('/').pop(); 
        const urlImagenFinal = `http://localhost:8080/img/productos/${nombreArchivo}`;

        listaContenedor.innerHTML += `
            <div class="list-group-item d-flex justify-content-between align-items-center p-3 border-0 border-bottom">
                <div class="d-flex align-items-center">
                    <img src="${urlImagenFinal}" width="60" height="60" class="me-3 rounded shadow-sm object-fit-cover" 
                         onerror="this.onerror=null; this.src='https://placehold.jp/150x150.png?text=Error';">
                    <div>
                        <h6 class="mb-0 fw-bold">${item.nombre}</h6>
                        <small class="text-muted">S/ ${item.precio.toFixed(2)} x ${item.cantidad}</small>
                    </div>
                </div>
                <div class="text-end">
                    <span class="d-block mb-2 fw-bold">S/ ${subtotalItem.toFixed(2)}</span>
                    <div class="btn-group shadow-sm">
                        <button class="btn btn-sm btn-light border" onclick="actualizarCantidad(${item.idProducto}, -1)">-</button>
                        <button class="btn btn-sm btn-light border" onclick="actualizarCantidad(${item.idProducto}, 1)">+</button>
                        <button class="btn btn-sm btn-outline-danger ms-2" onclick="eliminarProducto(${item.idProducto})"><i class="fa fa-trash"></i></button>
                    </div>
                </div>
            </div>`;
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

// 2. LÓGICA DE CULQI
window.abrirPasarela = function() {
    const totalStr = document.getElementById("totalCart").innerText;
    const totalCentavos = Math.round(parseFloat(totalStr) * 100); 
    const nroDoc = document.getElementById("nroDocumento").value;
    const tipoDoc = document.querySelector('input[name="tipoDoc"]:checked')?.value;

    if (obtenerCarrito().length === 0) return alert("El carrito está vacío.");
    if (tipoDoc === "FACTURA" && nroDoc.length !== 11) return alert("RUC inválido (11 dígitos).");
    if (tipoDoc === "BOLETA" && nroDoc.length !== 8) return alert("DNI inválido (8 dígitos).");

    Culqi.settings({
        title: 'TOISHAN S.A.C.',
        currency: 'PEN',
        description: 'Compra de Repuestos Automotrices',
        amount: totalCentavos
    });

    // Opciones limpias para evitar errores de carga de logo externo
    Culqi.options({
        lang: 'auto',
        installments: false,
        style: {
            maincolor: '#0d6efd',
            buttontext: '#ffffff'
        }
    });

    Culqi.open(); 
};

window.culqi = async function() {
    if (Culqi.token) { 
        const tokenID = Culqi.token.id;
        const email = Culqi.token.email;
        registrarVentaBackend("TARJETA", tokenID, email);
    } else {
        console.error("Error Culqi:", Culqi.error);
        alert("Pago no procesado: " + Culqi.error.user_message);
    }
};

// 3. ENVÍO AL BACKEND (RESOLVIENDO ERROR DE VALORES NULOS)
async function registrarVentaBackend(metodo, culqiToken = null, culqiEmail = null) {
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    const carritoActual = obtenerCarrito();
    // Capturamos montos asegurando que sean números (no strings)
    const totalVenta = parseFloat(document.getElementById("totalCart").innerText);
    const subtotalVenta = parseFloat(document.getElementById("subtotalCart").innerText);
    const igvVenta = parseFloat(document.getElementById("igvCart").innerText);
    
    const carrito = obtenerCarrito();

    const ventaDTO = {
        idCliente: parseInt(usuario.idPersona),
        tipoComprobante: document.querySelector('input[name="tipoDoc"]:checked').value.toUpperCase(),
        nroDocumento: document.getElementById("nroDocumento").value,
        metodoPago: metodo,
        
        total: totalVenta, 
        subtotal: subtotalVenta,
        igv: igvVenta,
        
        tokenCulqi: culqiToken,
        emailCulqi: culqiEmail,
        detalles: carrito.map(item => ({
            idProducto: parseInt(item.idProducto),
            cantidad: parseInt(item.cantidad),
            precioUnitario: parseFloat(item.precio)
        }))
    };

    const btn = document.getElementById("btnPagar");
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Procesando...';

    try {
        const res = await fetch("http://localhost:8080/producto/ventas/registrar", {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(ventaDTO)
        });

        if (res.ok) {
            const ventaFinal = await res.json();
            alert("¡Compra exitosa! Su pedido ha sido registrado.");
            generarComprobantePDF(ventaFinal, carritoActual);
            localStorage.removeItem("carrito");
            window.location.href = "index.html";
        } else {
            const errorMsg = await res.text();
            // Esto te dirá exactamente qué campo falta si Java sigue rechazando
            alert("Error en el servidor: " + errorMsg);
        }
    } catch (e) {
        alert("Error de conexión con el servidor TOISHAN.");
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<i class="fa fa-credit-card"></i> Pagar ahora';
    }
}

function generarComprobantePDF(v, carritoAux) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    
    // Configuración de estilo
    doc.setFontSize(18);
    doc.text("TOISHAN S.A.C.", 105, 20, { align: "center" });
    
    doc.setFontSize(12);
    doc.text(`Comprobante: ${v.tipoComprobante}`, 20, 40);
    doc.text(`Fecha: ${new Date().toLocaleDateString()}`, 20, 50);
    doc.text(`ID Venta: ${v.idVenta || 'N/A'}`, 20, 60);

    doc.text("Detalle de Productos:", 20, 80);
    doc.line(20, 82, 190, 82);

    let yPos = 90; 
    
    carritoAux.forEach((item) => {
        const nombre = item.nombre; 
        const cant = item.cantidad;
        const precioUnit = item.precio;
        const subtotalItem = (precioUnit * cant).toFixed(2);

        const linea = `${cant} x ${nombre}`;
        const precioTexto = `S/ ${subtotalItem}`;
        
        doc.text(linea, 20, yPos);
        doc.text(precioTexto, 170, yPos, { align: "right" });
        
        yPos += 10;

        // Control básico de espacio en la página
        if (yPos > 270) {
            doc.addPage();
            yPos = 20;
        }
    });

    // El Total final lo seguimos sacando de 'v' (la respuesta oficial del backend)
    doc.line(20, yPos, 190, yPos); 
    doc.setFont("helvetica", "bold");
    doc.text(`TOTAL A PAGAR: S/ ${v.total.toFixed(2)}`, 190, yPos + 10, { align: "right" });

    doc.save(`${v.tipoComprobante}_${v.idVenta || 'Ticket'}.pdf`);
}

window.actualizarCantidad = function(id, cambio) {
    let carrito = obtenerCarrito();
    const item = carrito.find(p => p.idProducto === id);
    if (item) {
        const nuevaCant = item.cantidad + cambio;
        if (nuevaCant > item.stock) return alert("Sin stock suficiente.");
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