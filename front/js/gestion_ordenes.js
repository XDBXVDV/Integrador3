const API_ORDENES = "http://localhost:8080/api/ordenes";
const API_FACTURAS = "http://localhost:8080/api/facturas";
let modalF = null;

document.addEventListener("DOMContentLoaded", () => {
    modalF = new bootstrap.Modal(document.getElementById('modalFactura'));
    cargarOrdenes();
});

async function cargarOrdenes() {
    const res = await fetch(`${API_ORDENES}/estado/EMITIDA`);
    const data = await res.json();
    const tbody = document.getElementById("tablaOrdenes");
    tbody.innerHTML = "";

    data.forEach(o => {
        tbody.innerHTML += `
            <tr>
                <td>${o.nroOrden}</td>
                <td>${o.cotizacion.proveedor.razonSocial}</td>
                <td>S/ ${o.total.toFixed(2)}</td>
                <td>${new Date(o.fechaEmision).toLocaleDateString()}</td>
                <td>
                    <button class="btn btn-sm btn-success" onclick="abrirModal(${o.idOrdenCompra})">
                        <i class="fas fa-file-upload"></i> Subir Factura
                    </button>
                </td>
            </tr>
        `;
    });
}

function abrirModal(id) {
    document.getElementById("idOrdenSeleccionada").value = id;
    modalF.show();
}

async function procesarFactura() {
    const id = document.getElementById("idOrdenSeleccionada").value;
    const serie = document.getElementById("serie").value;
    const corr = document.getElementById("correlativo").value;
    const file = document.getElementById("archivoPdf").files[0];

    if (!serie || !corr || !file) return alert("Complete todos los campos");

    const formData = new FormData();
    formData.append("idOrden", id);
    formData.append("serie", serie);
    formData.append("correlativo", corr);
    formData.append("archivo", file);

    try {
        const res = await fetch("http://localhost:8080/api/facturas/registrar", {
            method: 'POST',
            body: formData 
       });

        if (res.ok) {
            alert("Factura registrada e Inventario actualizado.");
            location.reload();
        } else {
            const error = await res.text();
            alert("Error: " + error);
        }
    } catch (e) { alert("Error al conectar con el servidor"); }
}