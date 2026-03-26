const API_URL = "http://localhost:8080";

document.addEventListener("DOMContentLoaded", () => {
    cargarFiltrosIniciales();
    cargarInventario();
});

async function cargarFiltrosIniciales() {
    try {
        const [resCat, resMar] = await Promise.all([
            fetch(`${API_URL}/categoria/listar`),
            fetch(`${API_URL}/marca/listar`)
        ]);
        const categorias = await resCat.json();
        const marcas = await resMar.json();
        const selCat = document.getElementById("filtCategoria");
        const selMar = document.getElementById("filtMarca");
        categorias.forEach(c => selCat.innerHTML += `<option value="${c.idCategoria}">${c.nombre}</option>`);
        marcas.forEach(m => selMar.innerHTML += `<option value="${m.idMarca}">${m.nombre}</option>`);
    } catch (error) {
        console.error("Error cargando filtros:", error);
    }
}

async function cargarInventario() {
   
    const nombre = document.getElementById("busqNombre").value;
    const idCat = document.getElementById("filtCategoria").value;
    const idMar = document.getElementById("filtMarca").value;
    const estado = document.getElementById("filtEstado").value;

    if (nombre.length > 0 && nombre.length < 2) return;

    let params = new URLSearchParams();
    if (nombre) params.append("nombre", nombre); 
    if (idCat) params.append("idCategoria", idCat);
    if (idMar) params.append("idMarca", idMar);
    if (estado) params.append("estado", estado);

    try {
        const res = await fetch(`${API_URL}/producto/inventario-filtrado?${params.toString()}`);
        const productos = await res.json();
        
        const tbody = document.getElementById("tablaInventario");
        tbody.innerHTML = "";

        if (productos.length === 0) {
            tbody.innerHTML = `<tr><td colspan="7" class="text-center py-5 text-muted">
                <i class="fas fa-search fa-2x mb-2"></i><br>No se encontraron productos con esos criterios.</td></tr>`;
            return;
        }

        productos.forEach(p => {
            const condClass = p.condicion === 'Agotado' ? 'bg-danger' : 
                             (p.condicion === 'Stock_bajo' ? 'bg-warning text-dark' : 'bg-success');
            const estadoClass = p.estado === 'Activo' ? 'text-success border-success' : 'text-secondary border-secondary';

            tbody.innerHTML += `
                <tr>
                    <td class="ps-4"><span class="text-muted small">#${p.idProducto}</span></td>
                    <td><span class="fw-bold">${p.nombre}</span></td>
                    <td><span class="badge bg-light text-dark border">${p.categoria.nombre}</span> 
                        <span class="badge bg-light text-dark border">${p.marca.nombre}</span></td>
                    <td class="text-center"><h5 class="mb-0 fw-bold">${p.stock}</h5></td>
                    <td class="text-center text-muted">${p.stockMinimo}</td>
                    <td><span class="badge border ${estadoClass}">${p.estado}</span></td>
                    <td><span class="badge ${condClass}">${p.condicion.replace('_', ' ')}</span></td>
                </tr>`;
        });
    } catch (error) {
        console.error("Error al cargar inventario:", error);
    }
}

function descargarReporteInventario() {
    const nombre = document.getElementById("busqNombre").value;
    const idCat = document.getElementById("filtCategoria").value;
    const idMar = document.getElementById("filtMarca").value;
    const estado = document.getElementById("filtEstado").value;

    let params = new URLSearchParams();
    if (nombre) params.append("nombre", nombre); 
    if (idCat) params.append("idCategoria", idCat);
    if (idMar) params.append("idMarca", idMar);
    if (estado) params.append("estado", estado);

    window.open(`${API_URL}/producto/inventario-reporte-pdf?${params.toString()}`, '_blank');
}

function resetFiltros() {

    document.getElementById("busqNombre").value = "";
    document.getElementById("filtCategoria").value = "";
    document.getElementById("filtMarca").value = "";
    document.getElementById("filtEstado").value = "Activo";
    cargarInventario();
}