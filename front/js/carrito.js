document.addEventListener('DOMContentLoaded', () => {
    renderizarCarrito();
});

function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('carrito')) || [];
}

function renderizarCarrito() {
    const carrito = obtenerCarrito();
    const tabla = document.getElementById('lista-carrito');
    const totalSpan = document.getElementById('total-carrito');
    let totalGeneral = 0;

    tabla.innerHTML = '';

    if (carrito.length === 0) {
        tabla.innerHTML = '<tr><td colspan="6" class="text-center">El carrito está vacío</td></tr>';
        totalSpan.innerText = '0.00';
        return;
    }

    carrito.forEach(item => {
        const subtotal = item.precio * item.cantidad;
        totalGeneral += subtotal;

        tabla.innerHTML += `
            <tr>
                <td><img src="${item.imagen}" width="50" style="border-radius:5px"></td>
                <td>
                    <strong>${item.nombre}</strong><br>
                    <small class="text-muted">Stock: ${item.stock}</small>
                </td>
                <td>S/ ${item.precio.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-outline-dark" onclick="actualizarCantidad(${item.idProducto}, -1)">-</button>
                    <span class="mx-2">${item.cantidad}</span>
                    <button class="btn btn-sm btn-outline-dark" onclick="actualizarCantidad(${item.idProducto}, 1)" 
                            ${item.cantidad >= item.stock ? 'disabled' : ''}>+</button>
                </td>
                <td>S/ ${subtotal.toFixed(2)}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="eliminarProducto(${item.idProducto})">Eliminar</button>
                </td>
            </tr>
        `;
    });

    totalSpan.innerText = totalGeneral.toFixed(2);
    if(typeof actualizarContadorNavbar === 'function') actualizarContadorNavbar();
}

async function finalizarCompra() {
    const carrito = obtenerCarrito();
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    const metodoPago = document.getElementById('selectMetodoPago').value;

    if (carrito.length === 0) return alert("Tu carrito está vacío.");
    
    // CAMBIO AQUÍ: Validamos idPersona en lugar de id_cliente
    if (!usuario || !usuario.idPersona) {
        alert("Error de sesión o perfil no encontrado. Por favor, inicia sesión nuevamente.");
        return;
    }

    // Armamos el DTO para el Backend
    const ventaRequest = {
        idCliente: usuario.idPersona, // CAMBIO AQUÍ: idPersona es el ID del Cliente
        total: parseFloat(document.getElementById('total-carrito').innerText),
        metodoPago: metodoPago,
        detalles: carrito.map(item => ({
            idProducto: item.idProducto,
            cantidad: item.cantidad,
            precioUnitario: item.precio,
            subtotal: item.precio * item.cantidad
        }))
    };

    try {
        const response = await fetch("http://localhost:8080/producto/ventas/registrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ventaRequest)
        });

        if (response.ok) {
            const data = await response.json();
            alert(`¡Venta #${data.idVenta} registrada con éxito!`);
            
            localStorage.removeItem('carrito');
            window.location.href = "index.html";
        } else {
            const errorMsg = await response.text();
            alert("Error: " + errorMsg);
        }
    } catch (error) {
        console.error("Error en la petición:", error);
        alert("No se pudo conectar con el servidor.");
    }
}

window.actualizarCantidad = function(id, cambio) {
    let carrito = obtenerCarrito();
    const item = carrito.find(p => p.idProducto === id);
    if (item) {
        const nuevaCant = item.cantidad + cambio;
        if (nuevaCant > item.stock) return alert("Límite de stock alcanzado");
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