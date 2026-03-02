const API_EMPLEADO = "http://localhost:8080/empleado";
const API_USUARIO = "http://localhost:8080/usuario";

document.addEventListener("DOMContentLoaded", () => {
    cargarEmpleados();
});

async function cargarEmpleados() {
    try {
        const res = await fetch(`${API_EMPLEADO}/listar`);
        if (!res.ok) throw new Error("Error al cargar empleados");

        const empleados = await res.json();
        const tbody = document.querySelector("#tablaEmpleados tbody");
        tbody.innerHTML = "";

        empleados.forEach(e => {
            const tr = document.createElement("tr");

            if (e.estadoUsuario !== "Activo") {
                tr.classList.add("inactivo");
            }

            tr.innerHTML = `
                
                <td>${e.nombre} ${e.apellido}</td>
                <td>${e.dni ?? ""}</td>
                <td>${e.rol}</td>
                <td>${e.estadoUsuario}</td>
                <td>
                    <button onclick="verDetalle(${e.idEmpleado})">
                        Ver
                    </button>

                    ${
                        e.estadoUsuario === "Activo"
                        ? `<button onclick="desactivar(${e.idUsuario})">Desactivar</button>`
                        : `<button onclick="activar(${e.idUsuario})">Activar</button>`
                    }
                </td>
            `;

            tbody.appendChild(tr);
        });

    } catch (err) {
        alert(err.message);
    }
}
function verDetalle(idEmpleado) {
    window.location.href = `detalle_empleado.html?id=${idEmpleado}`;
}

async function desactivar(idUsuario) {
    if (!confirm("¿Desactivar este empleado?")) return;

    const res = await fetch(`${API_USUARIO}/desactivar/${idUsuario}`, {
        method: "PUT"
    });

    if (!res.ok) {
        alert("Error al desactivar");
        return;
    }

    alert("Empleado desactivado");
    cargarEmpleados();
}

async function activar(idUsuario) {
    if (!confirm("¿Activar este empleado?")) return;

    const res = await fetch(`${API_USUARIO}/reactivar/${idUsuario}`, {
        method: "PUT"
    });

    if (!res.ok) {
        alert("Error al activar");
        return;
    }

    alert("Empleado activado");
    cargarEmpleados();
}