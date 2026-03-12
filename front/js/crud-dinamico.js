const CrudDinamico = {
    api: "",
    tabla: "",
    idCampo: "",
    columnas: [],
    formCrear: [],
    formEditar: [],
    datos: [],

    init(config) {
        this.api = config.api;
        this.tabla = config.tabla;
        this.idCampo = config.idCampo;
        this.columnas = config.columnas;
        this.formCrear = config.formCrear;
        this.formEditar = config.formEditar;
        this.listar();
    },

    async listar() {
        try {
            const res = await fetch(`${this.api}/listar`);
            if (!res.ok) throw new Error("Error al obtener datos");
            this.datos = await res.json();
            this.renderTabla();
        } catch (err) {
            console.error("Fallo al listar:", err);
        }
    },

    renderTabla() {
        const tbody = document.querySelector(this.tabla + " tbody");
        tbody.innerHTML = "";

        this.datos.forEach(item => {
            const fila = document.createElement("tr");
            let html = "";

            this.columnas.forEach(col => {
                if (col === "estado") {
                    const color = item.estado === "Activo" ? "success" : "danger";
                    html += `<td><span class="badge bg-${color}">${item.estado}</span></td>`;
                } else {
                    html += `<td>${item[col] ?? "---"}</td>`;
                }
            });

            const botonEstado = item.estado === "Activo"
                ? `<button class="btn btn-sm btn-danger" onclick="CrudDinamico.desactivar(${item[this.idCampo]})">Desactivar</button>`
                : `<button class="btn btn-sm btn-success" onclick="CrudDinamico.activar(${item[this.idCampo]})">Activar</button>`;

            html += `
                <td>
                    <div class="d-flex gap-1">
                        <button class="btn btn-sm btn-warning" onclick="CrudDinamico.abrirEditar(${item[this.idCampo]})">Editar</button>
                        ${botonEstado}
                    </div>
                </td>
            `;
            fila.innerHTML = html;
            tbody.appendChild(fila);
        });
    },

    async activar(id) {
        // Cambiado a /reactivar/ para coincidir con tu controlador
        await fetch(`${this.api}/reactivar/${id}`, { method: "PUT" });
        this.listar();
    },

    async desactivar(id) {
        await fetch(`${this.api}/desactivar/${id}`, { method: "PUT" });
        this.listar();
    },

    async abrirEditar(id) {
        try {
            const res = await fetch(`${this.api}/buscar/${id}`);
            const data = await res.json();
            
            this.formEditar.forEach(campo => {
                const input = document.getElementById("edit" + campo);
                if (input) input.value = data[campo] ?? "";
            });
            document.getElementById("modalEditar").style.display = "flex";
        } catch (err) { alert("Error al buscar proveedor"); }
    },

    async guardarEdicion() {
        const id = document.getElementById("edit" + this.idCampo).value;
        const data = {};
        this.formEditar.forEach(campo => {
            data[campo] = document.getElementById("edit" + campo).value;
        });

        await fetch(`${this.api}/actualizar/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        document.getElementById("modalEditar").style.display = "none";
        this.listar();
    },

    async guardarNuevo() {
        const data = {};
        this.formCrear.forEach(campo => {
            data[campo] = document.getElementById("create" + campo).value;
        });

        const res = await fetch(`${this.api}/crear`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            document.getElementById("modalCrear").style.display = "none";
            document.getElementById("formCrearInputs").reset(); // Limpiar form
            this.listar();
        } else {
            alert("Error al crear proveedor");
        }
    }
};