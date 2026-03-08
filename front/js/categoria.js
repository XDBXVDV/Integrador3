const API_URL = "http://localhost:8080/categoria";

document.addEventListener("DOMContentLoaded", () => {
    listarCategorias();
});


/* =========================
   LISTAR CATEGORIAS
========================= */

function listarCategorias() {

    fetch(API_URL + "/listar")
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
                        <button onclick="abrirModalEditar(${c.idCategoria})">
                            Editar
                        </button>
                        ${botonEstado}
                    </td>
                `;

                tbody.appendChild(fila);

            });

        })
        .catch(err => alert(err.message));
}


/* =========================
   DESACTIVAR
========================= */

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


/* =========================
   REACTIVAR
========================= */

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


/* =========================
   ABRIR MODAL EDITAR
========================= */

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


/* =========================
   CERRAR MODAL EDITAR
========================= */

function cerrarModal() {

    document.getElementById("modalEditar").style.display = "none";

}


/* =========================
   GUARDAR EDICION
========================= */

function guardarEdicion() {

    const id = document.getElementById("editIdCategoria").value;

    const data = {
        nombre: document.getElementById("editNombre").value
    };

    fetch(`${API_URL}/actualizar/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("Error al actualizar categoria");

            cerrarModal();
            listarCategorias();
        })
        .catch(err => alert(err.message));

}


/* =========================
   ABRIR MODAL CREAR
========================= */

function abrirModalCrear() {

    document.getElementById("modalCrear").style.display = "flex";

}


/* =========================
   CERRAR MODAL CREAR
========================= */

function cerrarModalCrear() {

    document.getElementById("modalCrear").style.display = "none";

    limpiarFormularioCrear();

}


/* =========================
   GUARDAR CATEGORIA
========================= */

function guardarCategoria() {

    const nombre = document.getElementById("createNombre").value;

    if (!nombre) {
        alert("El nombre es obligatorio");
        return;
    }

    const data = { nombre };

    fetch(`${API_URL}/crear`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(res => {

            if (!res.ok) throw new Error("Error al crear categoria");

            cerrarModalCrear();
            listarCategorias();

        })
        .catch(err => alert(err.message));

}


/* =========================
   LIMPIAR FORMULARIO
========================= */

function limpiarFormularioCrear() {

    document.getElementById("createNombre").value = "";

}


/* =========================
   CERRAR MODALES AL HACER CLICK FUERA
========================= */

window.onclick = function (event) {

    const modalCrear = document.getElementById("modalCrear");
    const modalEditar = document.getElementById("modalEditar");

    if (event.target === modalCrear) {
        modalCrear.style.display = "none";
    }

    if (event.target === modalEditar) {
        modalEditar.style.display = "none";
    }

};

function filtrarTabla(){

const input = document.getElementById("buscadorTabla");

const filtro = input.value.toLowerCase();

const filas = document.querySelectorAll("#tablaCategorias tbody tr");

filas.forEach(fila => {

const texto = fila.innerText.toLowerCase();

if(texto.includes(filtro)){
fila.style.display = "";
}else{
fila.style.display = "none";
}

});

}