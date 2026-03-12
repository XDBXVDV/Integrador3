const contenedor = document.getElementById("detalleProducto");
let productoActual = null; // Guardará el objeto completo de la API

document.addEventListener("DOMContentLoaded", cargarProducto);

function obtenerId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function cargarProducto() {
    const id = obtenerId();
    if (!id) {
        contenedor.innerHTML = "<div class='alert alert-danger'>ID de producto no válido.</div>";
        return;
    }

    try {
        // Listar
        const res = await fetch("http://localhost:8080/producto/listarDTO");
        const productos = await res.json();
        
        productoActual = productos.find(prod => prod.idProducto == id);

        if (!productoActual) {
            contenedor.innerHTML = "<div class='alert alert-warning'>Producto no encontrado.</div>";
            return;
        }

        // Carga de datos
        const imagenUrl = productoActual.imagen
            ? "http://localhost:8080" + productoActual.imagen
            : "http://localhost:8080/img/productos/default.png";

        const tieneStock = productoActual.stock > 0;
        
        // Validar Rol de Usuario
        const usuario = JSON.parse(localStorage.getItem('usuario'));
        const esInvitado = !usuario;
        const esCliente = usuario && usuario.rolName.toUpperCase() === "CLIENTE";
        const esEmpleado = usuario && usuario.rolName.toUpperCase() !== "CLIENTE";

        contenedor.innerHTML = `
            <div class="row mt-5">
                <div class="col-md-6 text-center">
                    <img src="${imagenUrl}" class="img-fluid rounded shadow" style="max-height: 400px;">
                </div>
                <div class="col-md-6">
                    <span class="badge ${tieneStock ? 'bg-success' : 'bg-danger'} mb-2">
                        ${tieneStock ? 'En Stock' : 'Agotado'}
                    </span>
                    <h2 class="fw-bold">${productoActual.nombre}</h2>
                    <p class="text-muted">${productoActual.descripcion || 'Sin descripción disponible.'}</p>
                    <h3 class="text-primary mb-4">S/ ${productoActual.precio.toFixed(2)}</h3>
                    
                    <div class="p-3 border rounded bg-light">
                        <p><strong>Stock disponible:</strong> ${productoActual.stock} unidades</p>
                        
                        ${esEmpleado ? `
                            <div class="alert alert-info small">
                                 Estás en modo gestión. Los empleados no pueden realizar compras.
                            </div>
                        ` : `
                            <div class="d-flex align-items-center gap-3 mb-3">
                                <label for="cantidad">Cantidad:</label>
                                <input type="number" id="cantidad" class="form-control" 
                                       value="${tieneStock ? 1 : 0}" min="1" max="${productoActual.stock}" 
                                       style="width: 80px;" ${!tieneStock ? 'disabled' : ''}>
                            </div>

                            <button onclick="agregarAlCarrito()" class="btn btn-primary btn-lg w-100" 
                                    ${!tieneStock ? 'disabled' : ''}>
                                ${esInvitado ? 'Inicia sesión para comprar' : 'Agregar al carrito'}
                            </button>
                        `}
                    </div>
                    <div class="mt-3">
                        <a href="index.html" class="text-decoration-none">← Volver a la tienda</a>
                    </div>
                </div>
            </div>
        `;
    } catch (error) {
        console.error("Error al cargar producto:", error);
        contenedor.innerHTML = "<div class='alert alert-danger'>Error al conectar con el servidor.</div>";
    }
}

function agregarAlCarrito() {
    // Validar Sesión
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    if (!usuario) {
        alert("Debes iniciar sesión para agregar productos.");
        window.location.href = "login.html";
        return;
    }

    // Validar que sea Cliente
    if (usuario.rolName.toUpperCase() !== "CLIENTE") {
        alert("Solo los clientes pueden usar el carrito de compras.");
        return;
    }

    const inputCantidad = document.getElementById("cantidad");
    const cantidadSeleccionada = parseInt(inputCantidad.value);

    if (isNaN(cantidadSeleccionada) || cantidadSeleccionada <= 0) {
        alert("Por favor, selecciona una cantidad válida.");
        return;
    }

    //  Obtener carrito y validar Stock acumulado
    let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
    const index = carrito.findIndex(item => item.idProducto === productoActual.idProducto);
    const cantidadEnCarrito = index !== -1 ? carrito[index].cantidad : 0;

    if (cantidadEnCarrito + cantidadSeleccionada > productoActual.stock) {
        alert(`No hay suficiente stock. Ya tienes ${cantidadEnCarrito} en el carrito y el máximo es ${productoActual.stock}.`);
        return;
    }

    //  Guardar en LocalStorage
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

    alert(`¡Se agregaron ${cantidadSeleccionada} unidad(es) de ${productoActual.nombre}!`);
}