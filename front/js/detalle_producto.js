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

        const res = await fetch("http://localhost:8080/producto/listarDTO");
        const productos = await res.json();
        
        productoActual = productos.find(prod => prod.idProducto == id);

        if (!productoActual) {
            contenedor.innerHTML = "<div class='alert alert-warning'>Producto no encontrado.</div>";
            return;
        }

        const imagenUrl = productoActual.imagen
            ? "http://localhost:8080" + productoActual.imagen
            : "http://localhost:8080/img/productos/default.png";

        const tieneStock = productoActual.stock > 0;

        const usuario = JSON.parse(localStorage.getItem('usuario'));
        const esInvitado = !usuario;
        const esCliente = usuario && usuario.rolName.toUpperCase() === "CLIENTE";
        const esEmpleado = usuario && usuario.rolName.toUpperCase() !== "CLIENTE";

        contenedor.innerHTML = `
<div class="producto-detalle">

    <img src="${imagenUrl}">

    <div class="producto-info">

        <span class="badge-stock ${tieneStock ? 'stock-ok' : 'stock-no'}">
            ${tieneStock ? 'En Stock' : 'Agotado'}
        </span>

        <h2>${productoActual.nombre}</h2>

        <p>${productoActual.descripcion || 'Sin descripción disponible.'}</p>

        <div class="precio">S/ ${productoActual.precio.toFixed(2)}</div>

    </div>

</div>

<div class="compra-box">

    <p><strong>Stock:</strong> ${productoActual.stock}</p>

    ${esEmpleado ? `
        <p style="font-size:13px;color:#555;">
        Estás en modo gestión. No puedes comprar.
        </p>
    ` : `
        <div>
            <label>Cantidad:</label><br>
            <input type="number" id="cantidad"
                value="${tieneStock ? 1 : 0}"
                min="1"
                max="${productoActual.stock}"
                ${!tieneStock ? 'disabled' : ''}>
        </div>

        <button onclick="agregarAlCarrito()" class="btn-primary"
            ${!tieneStock ? 'disabled' : ''}>
            ${esInvitado ? 'Inicia sesión para comprar' : 'Agregar al carrito'}
        </button>
    `}
</div>
`;
    } catch (error) {
        console.error("Error al cargar producto:", error);
        contenedor.innerHTML = "<div class='alert alert-danger'>Error al conectar con el servidor.</div>";
    }
}

function agregarAlCarrito() {

    const usuario = JSON.parse(localStorage.getItem('usuario'));
    if (!usuario) {
        alert("Debes iniciar sesión para agregar productos.");
        window.location.href = "login.html";
        return;
    }

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

    let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
    const index = carrito.findIndex(item => item.idProducto === productoActual.idProducto);
    const cantidadEnCarrito = index !== -1 ? carrito[index].cantidad : 0;

    if (cantidadEnCarrito + cantidadSeleccionada > productoActual.stock) {
        alert(`No hay suficiente stock. Ya tienes ${cantidadEnCarrito} en el carrito y el máximo es ${productoActual.stock}.`);
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

    alert(`¡Se agregaron ${cantidadSeleccionada} unidad(es) de ${productoActual.nombre}!`);
}