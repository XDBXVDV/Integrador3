const API_URL = "http://localhost:8080/proveedor";

document.addEventListener("DOMContentLoaded", () => {
    listarProveedores();
});

function listarProveedores() {

    fetch(API_URL+"/listar")
        .then(res => {
            if (!res.ok) throw new Error("Error al obtener proveedores");
            return res.json();
        })
        .then(data => {
            const tbody = document.querySelector("#tablaProveedores tbody");
            tbody.innerHTML = "";

            data.forEach(p => {
                const fila = document.createElement("tr");
let botonEstado = "";

if (p.estado === "Activo") {
    botonEstado = `
        <button onclick="desactivarProveedor(${p.idProveedor})">
            Desactivar
        </button>
    `;
} else {
    botonEstado = `
        <button onclick="reactivarProveedor(${p.idProveedor})">
            Reactivar
        </button>
    `;
}
                fila.innerHTML = `
                    <td>${p.idProveedor}</td>
                    <td>${p.razonSocial}</td>
                    <td>${p.ruc}</td>
                    <td>${p.telefono ?? ""}</td>
                    <td>${p.direccion ?? ""}</td>
                    <td>${p.email ?? ""}</td>
                    <td>${p.estado}</td>
                    <td>
                        <button onclick="abrirModalEditar(${p.idProveedor})">Editar</button>
                        ${botonEstado}
                    </td>
                `;

                tbody.appendChild(fila);
            });
        })
        .catch(err => alert(err.message));
}

function desactivarProveedor(id) {

    if (!confirm("¿Desea desactivar este proveedor?")) return;

    fetch(`${API_URL}/desactivar/${id}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al desactivar");
        listarProveedores();
    })
    .catch(err => alert(err.message));
}


function reactivarProveedor(id) {

    if (!confirm("¿Desea reactivar este proveedor?")) return;

    fetch(`${API_URL}/reactivar/${id}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al reactivar");
        listarProveedores();
    })
    .catch(err => alert(err.message));
}

function abrirModalEditar(id) {

    fetch(`${API_URL}/buscar/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("No se pudo obtener proveedor");
            return res.json();
        })
        .then(p => {
            document.getElementById("editIdProveedor").value = p.idProveedor;
            document.getElementById("editRazonSocial").value = p.razonSocial;
            document.getElementById("editTelefono").value = p.telefono ?? "";
            document.getElementById("editDireccion").value=p.direccion??"";
            document.getElementById("editEmail").value = p.email ?? "";
            
            document.getElementById("modalEditar").style.display = "flex";
        })
        .catch(err => alert(err.message));
}

function cerrarModal() {
    document.getElementById("modalEditar").style.display = "none";
}

function guardarEdicion() {

    const id = document.getElementById("editIdProveedor").value;

    const data = {
        razonSocial: document.getElementById("editRazonSocial").value,
        telefono: document.getElementById("editTelefono").value,
        direccion: document.getElementById("editDireccion").value,
        email: document.getElementById("editEmail").value
    };

    fetch(`${API_URL}/actualizar/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al actualizar proveedor");
        cerrarModal();
        listarProveedores();
    })
    .catch(err => alert(err.message));
}

function abrirModalCrear() {
    document.getElementById("modalCrear").style.display = "block";
}

function cerrarModalCrear() {
    document.getElementById("modalCrear").style.display = "none";
    limpiarFormularioCrear();
}

function guardarProveedor() {

    const data = {
        razonSocial: document.getElementById("createRazonSocial").value,
        ruc: document.getElementById("createRuc").value,
        telefono: document.getElementById("createTelefono").value,
        email: document.getElementById("createEmail").value,
        direccion: document.getElementById("createDireccion").value
    };

    if (!data.razonSocial || !data.ruc) {
        alert("Razón social y RUC son obligatorios");
        return;
    }

    fetch(`${API_URL}/crear`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al crear proveedor");
        cerrarModalCrear();
        listarProveedores();
    })
    .catch(err => alert(err.message));
}

function limpiarFormularioCrear() {
    document.getElementById("createRazonSocial").value = "";
    document.getElementById("createRuc").value = "";
    document.getElementById("createTelefono").value = "";
    document.getElementById("createEmail").value = "";
    document.getElementById("createDireccion").value = "";
}

window.onclick = function (event) {
    const modal = document.getElementById("modalCrear");
    if (event.target === modal) {
        modal.style.display = "none";
    }
};
window.onclick = function (event) {
    const modal = document.getElementById("modalEditar");
    if (event.target === modal) {
        modal.style.display = "none";
    }
};