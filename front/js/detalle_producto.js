const contenedor = document.getElementById("detalleProducto");
let productoActual = null; // Aquí guardamos el objeto completo que viene de la API

document.addEventListener("DOMContentLoaded", cargarProducto);

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarProducto() {
    const id = obtenerId();
    // Llamada a tu API de Spring Boot
    const res = await fetch("http://localhost:8080/producto/listarDTO");
    const productos = await res.json();
    
    // IMPORTANTE: Buscamos el producto y verificamos que tenga la propiedad .stock
    productoActual = productos.find(prod => prod.idProducto == id);

    if (!productoActual) {
        contenedor.innerHTML = "<p>Producto no encontrado</p>";
        return;
    }

    const imagen = productoActual.imagen
        ? "http://localhost:8080" + productoActual.imagen
        : "http://localhost:8080/img/productos/default.png";

    // Verificamos si hay stock disponible
    const tieneStock = productoActual.stock > 0;

    contenedor.innerHTML = `
        <div class="producto-detalle">
            <img src="${imagen}" width="300">
            <h2>${productoActual.nombre}</h2>
            <p>${productoActual.descripcion || 'Sin descripción'}</p>
            <p><strong>Precio:</strong> S/ ${productoActual.precio.toFixed(2)}</p>
            <p><strong>Stock disponible:</strong> <span id="stock-display">${productoActual.stock}</span></p>
            
            <div class="controles">
                <label for="cantidad">Cantidad:</label>
                <input type="number" 
                       min="1" 
                       max="${productoActual.stock}" 
                       value="${tieneStock ? 1 : 0}" 
                       id="cantidad" 
                       ${!tieneStock ? 'disabled' : ''}
                       class="form-control" style="width: 80px; display: inline-block;">
                
                <button onclick="agregarCarrito()" 
                        class="btn btn-primary" 
                        ${!tieneStock ? 'disabled' : ''}>
                    ${tieneStock ? 'Agregar al carrito' : 'Sin Stock'}
                </button>
            </div>
        </div>
    `;
}

function agregarCarrito() {
    // --- NUEVA VALIDACIÓN DE LOGIN ---
    // Ajusta 'usuario' al nombre de la clave que uses en tu localStorage
    const usuarioLogueado = JSON.parse(localStorage.getItem('usuario'));

    if (!usuarioLogueado) {
        alert("Debes iniciar sesión para agregar productos al carrito.");
        window.location.href = "login.html"; // Redirigir al login
        return;
    }
    // ---------------------------------

    if (!productoActual) return;

    const inputCantidad = document.getElementById("cantidad");
    const cantidadSeleccionada = parseInt(inputCantidad.value);

    // Obtener carrito actual
    let carrito = JSON.parse(localStorage.getItem('carrito')) || [];

    // Buscar si ya existe para validar el stock acumulado
    const index = carrito.findIndex(item => item.idProducto === productoActual.idProducto);
    const cantidadPrevia = index !== -1 ? carrito[index].cantidad : 0;

    // Validación de stock
    if (cantidadPrevia + cantidadSeleccionada > productoActual.stock) {
        alert(`Error: No puedes superar el stock disponible (${productoActual.stock}).`);
        return;
    }

    if (index !== -1) {
        carrito[index].cantidad += cantidadSeleccionada;
    } else {
        const imagenUrl = productoActual.imagen
            ? "http://localhost:8080" + productoActual.imagen
            : "http://localhost:8080/img/productos/default.png";

        carrito.push({
            idProducto: productoActual.idProducto,
            nombre: productoActual.nombre,
            precio: productoActual.precio,
            imagen: imagenUrl,
            cantidad: cantidadSeleccionada,
            stock: productoActual.stock 
        });
    }

    localStorage.setItem('carrito', JSON.stringify(carrito));
    
    if (typeof actualizarContadorNavbar === 'function') {
        actualizarContadorNavbar();
    }

    alert(`¡"${productoActual.nombre}" agregado correctamente!`);
}