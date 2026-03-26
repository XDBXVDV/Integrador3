const API_URL = "http://localhost:8081/api";

document.addEventListener("DOMContentLoaded", () => {
    cargarProductosCriticos();
});

async function cargarProductosCriticos() {
    try {
        // Usamos el endpoint que filtra por condición 'Agotado' o 'Stock_bajo'
        const res = await fetch(`http://localhost:8081/producto/faltantes`);
        const productos = await res.json();
        
        const tbody = document.getElementById("listaFaltantes");
        tbody.innerHTML = "";

        let agotados = 0;
        let bajos = 0;

        productos.forEach(p => {
            if (p.condicion === 'Agotado') agotados++;
            else bajos++;

            // Lógica de prioridad visual
            const badgeColor = p.stock === 0 ? 'bg-danger' : 'bg-warning text-dark';
            const prioridad = p.stock === 0 ? 'CRÍTICA' : 'ALTA';
            
            // Cantidad sugerida: (Mínimo - Actual) + 10 de reserva
            const sugerido = (p.stockMinimo - p.stock) + 10;

            tbody.innerHTML += `
    <tr>
        <td><input type="checkbox" class="form-check-input chk-producto" value="${p.idProducto}"></td>
        <td>
            <div class="fw-bold">${p.nombre}</div>
            <small class="text-muted">${p.categoria.nombre} | ${p.marca.nombre}</small>
        </td>
        <td class="text-center fw-bold ${p.stock === 0 ? 'text-danger' : ''}">${p.stock}</td>
        <td class="text-center">${p.stockMinimo}</td>
        <td><span class="badge ${badgeColor}">${prioridad}</span></td>
        <td>
            <input type="number" class="form-control form-control-sm input-cantidad" 
                   value="${sugerido}" style="width: 80px;" data-id="${p.idProducto}">
        </td>
    </tr>
`;
        });

        document.getElementById("countAgotados").innerText = agotados;
        document.getElementById("countBajos").innerText = bajos;

    } catch (error) {
        console.error("Error al cargar productos:", error);
    }
}

async function enviarRequerimientoMasivo() {
    const seleccionados = document.querySelectorAll(".chk-producto:checked");
    if (seleccionados.length === 0) return alert("Seleccione al menos un producto.");

    const usuarioStr = localStorage.getItem("usuario");
    if (!usuarioStr) return alert("Sesión no válida.");
    const usuario = JSON.parse(usuarioStr);

    const requerimientos = Array.from(seleccionados).map(cb => {
        // 1. Aseguramos que el ID del producto sea número
        const idProdNumerico = Number(cb.value);
        
        // 2. Capturamos la cantidad
        const inputCant = document.querySelector(`.input-cantidad[data-id="${cb.value}"]`);
        const cantNumerica = inputCant ? Number(inputCant.value) : 0;

        // 3. Buscamos el ID del empleado (prueba con id o idEmpleado)
        const idEmp = Number(usuario.idEmpleado || usuario.id || usuario.idUsuario);

        // VALIDACIÓN DE DEPURACIÓN
        if (isNaN(idProdNumerico)) console.error("ERROR: El checkbox no tiene un ID válido:", cb.value);
        if (isNaN(idEmp)) console.error("ERROR: El usuario del localStorage no tiene ID válido:", usuario);

        return {
            idProducto: idProdNumerico,
            cantidadSugerida: cantNumerica,
            idEmpleadoAlmacen: idEmp
        };
    });

    // Si hay algún NaN, detenemos la ejecución para no romper el backend
    if (requerimientos.some(r => isNaN(r.idProducto) || isNaN(r.idEmpleadoAlmacen))) {
        alert("Error de datos: Algunos IDs no son números válidos. Revisa la consola (F12).");
        return;
    }

    console.log("Datos listos para enviar:", requerimientos);

    try {
        const res = await fetch("http://localhost:8080/api/requerimientos/registrar-masivo", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requerimientos)
        });

        if (res.ok) {
            alert("Requerimientos enviados con éxito.");
            location.reload();
        } else {
            const errorText = await res.text();
            alert("Error del servidor: " + errorText);
        }
    } catch (error) {
        console.error("Error en la petición:", error);
    }
}

function toggleAll(source) {
    const checkboxes = document.querySelectorAll('.chk-producto');
    checkboxes.forEach(cb => cb.checked = source.checked);
}