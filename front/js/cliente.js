const API_CLIENTE = "http://localhost:8080/cliente";

document.getElementById("formCliente").addEventListener("submit", async (e) => {
    e.preventDefault();

    try {
        const res = await fetch(`${API_CLIENTE}/crear`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                usuario: document.getElementById("usuario").value,
                email: document.getElementById("email").value,
                contrasena: document.getElementById("contrasena").value,
                idRol: 1,

                nombre: document.getElementById("nombre").value,
                apellido: document.getElementById("apellido").value,
                dni: document.getElementById("dni").value,
                telefono: document.getElementById("telefono").value,
                direccion: document.getElementById("direccion").value
            })
        });

        if (!res.ok) throw new Error("Error al crear cliente");

        alert("Cliente registrado correctamente");
        e.target.reset();

    } catch (err) {
        alert(err.message);
    }
});