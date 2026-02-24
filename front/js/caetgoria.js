const API_URL = "http://localhost:8080/categoria";
document.addEventListener("DOMContentLoaded", () => {
    listarCategorias();
});

function listarCategorias() {

    fetch(API_URL+"/listar")
        .then(res => {
            if (!res.ok) throw new Error("Error al obtener categorias");
            return res.json();
        })
        .then(data => {
            const tbody = document.querySelector("#tablaCategorias tbody");
            tbody.innerHTML = "";

            data.forEach(c => {
                const fila = document.createElement("tr");
let botonEstado = "";

if (c.estado === "Activo") {
    botonEstado = `
        <button onclick="desactivarCategoria(${c.idCategoria})">
            Desactivar
        </button>
    `;
} else {
    botonEstado = `
        <button onclick="reactivarCategoria(${c.idCategoria})">
            Reactivar
        </button>
    `;
}
                fila.innerHTML = `
                    <td>${c.idCategoria}</td>
                    <td>${c.nombre}</td>
                    <td>${c.estado}</td>
                    <td>
                        <button onclick="abrirModalEditar(${c.idCategoria})">Editar</button>
                        ${botonEstado}
                    </td>
                `;

                tbody.appendChild(fila);
            });
        })
        .catch(err => alert(err.message));
}

function desactivarCategoria(id) {

    if (!confirm("¿Desea desactivar esta categoria?")) return;

    fetch(`${API_URL}/desactivar/${id}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al desactivar");
        listarCategorias();
    })
    .catch(err => alert(err.message));
}

function reactivarCategoria(id) {

    if (!confirm("¿Desea reactivar esta categoria?")) return;

    fetch(`${API_URL}/activar/${id}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al reactivar");
        listarCategorias();
    })
    .catch(err => alert(err.message));
}

function abrirModalEditar(id) {

    fetch(`${API_URL}/buscar/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("No se pudo obtener categoria");
            return res.json();
        })
        .then(c => {
            document.getElementById("editIdCategoria").value = c.idCategoria;
            document.getElementById("editNombre").value = c.nombre;            
            document.getElementById("modalEditar").style.display = "flex";
        })
        .catch(err => alert(err.message));
}

function cerrarModal() {
    document.getElementById("modalEditar").style.display = "none";
}

function guardarEdicion() {

    const id = document.getElementById("editIdCategoria").value;

    const data = {
        nombre: document.getElementById("editNombre").value,
    };

    fetch(`${API_URL}/actualizar/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al actualizar categoria");
        cerrarModal();
        listarCategorias();
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

function guardarCategoria() {

    const data = {
        nombre: document.getElementById("createNombre").value,
    };

    if (!data.nombre) {
        alert("El nombre es obligatorio");
        return;
    }

    fetch(`${API_URL}/crear`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al crear categoria");
        cerrarModalCrear();
        listarCategorias();
    })
    .catch(err => alert(err.message));
}

function limpiarFormularioCrear() {
    document.getElementById("createNombre").value = "";
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
