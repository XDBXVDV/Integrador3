const API_EMPLEADO = "http://localhost:8080/empleado";

document.getElementById("formEmpleado").addEventListener("submit", async (e) => {
    e.preventDefault();

  
    const usuarioEl = document.getElementById("usuario");
    const emailEl = document.getElementById("email");
    const passwordEl = document.getElementById("password");
    const rolEl = document.getElementById("rol");
    const nombreEl = document.getElementById("nombre");
    const apellidoEl = document.getElementById("apellido");
    const dniEl = document.getElementById("dni");


    if (
        !usuarioEl || !emailEl || !passwordEl ||
        !rolEl || !nombreEl || !apellidoEl || !dniEl
    ) {
        alert("Complete todos los campos");
        return;
    }

    
   
    
    const body = {
        usuario: usuarioEl.value,
        email: emailEl.value,
        contrasena: passwordEl.value, 
        idRol: Number(rolEl.value),
        nombre: nombreEl.value,
        apellido: apellidoEl.value,
        dni: dniEl.value
    };

    try {
        const res = await fetch(`${API_EMPLEADO}/crear`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            throw new Error(await res.text());
        }

        alert("Empleado creado correctamente");
        e.target.reset();

    } catch (err) {
        alert(err.message);
    }
});