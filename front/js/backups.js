const API_URL = "http://localhost:8080/api/dashboard";

document.addEventListener("DOMContentLoaded", () => {
    listarArchivos();
});

async function ejecutarNuevoBackup() {

    if(!confirm("¿Confirmas la creación de un nuevo punto de restauración?")) return;

    try {
        const res = await fetch(`${API_URL}/backup`, {
            
        });
        const msg = await res.text();
        alert(msg);
        listarArchivos(); // Recargar la lista
    } catch (error) {
        alert("Error al conectar con el servidor.");
    }
}

async function listarArchivos() {

    try {
        const res = await fetch(`${API_URL}/listar-backups`, {

        });
        const archivos = await res.json();
        
        const tbody = document.getElementById("listaBackups");
        tbody.innerHTML = "";

        archivos.forEach(nombre => {
            tbody.innerHTML += `
                <tr>
                    <td><i class="far fa-file-code text-secondary me-2"></i>${nombre}</td>
                    <td>${nombre.replace('toishan_backup_', '').replace('.sql', '')}</td>
                    <td class="text-end">
                        <button class="btn btn-outline-primary btn-sm" onclick="restaurar('${nombre}')">
                            <i class="fas fa-undo"></i> Restaurar
                        </button>
                        <button class="btn btn-danger btn-sm" onclick="eliminar('${nombre}')">
                <i class="fas fa-trash">Eliminar</i>
            </button>
                    </td>
                </tr>`;
        });
    } catch (e) { console.error(e); }
}

// Función para ELIMINAR
async function eliminar(nombre) {
    if (!confirm(`¿Estás seguro de eliminar el archivo ${nombre}? Esta acción no se puede deshacer.`)) return;

    const token = localStorage.getItem("token");
    try {
        const res = await fetch(`${API_URL}/eliminar-backup/${nombre}`, {
            method: "DELETE",
            headers: { "Authorization": `Bearer ${token}` }
        });
        const msg = await res.text();
        alert(msg);
        listarArchivos(); // Refrescar tabla
    } catch (error) {
        alert("Error al intentar eliminar.");
    }
}

async function restaurar(nombre) {
    const confirmacion = confirm(`⚠️ ¡ADVERTENCIA! ⚠️\nVas a restaurar la base de datos con el archivo: ${nombre}.\nLos datos actuales que no estén en este respaldo SE PERDERÁN.\n\n¿Deseas continuar?`);
    
    if (!confirmacion) return;

    const token = localStorage.getItem("token");
    
    const btnRestaurar = event.currentTarget;
    const originalContent = btnRestaurar.innerHTML;
    btnRestaurar.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
    btnRestaurar.disabled = true;

    try {
        const res = await fetch(`${API_URL}/restaurar-backup/${nombre}`, {
            method: "POST",
            headers: { "Authorization": `Bearer ${token}` }
        });
        const msg = await res.text();
        alert(msg);
    } catch (error) {
        alert("Error de conexión durante la restauración.");
    } finally {
        btnRestaurar.innerHTML = originalContent;
        btnRestaurar.disabled = false;
    }
}