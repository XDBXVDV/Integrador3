const API_AUTH = "http://localhost:8080/auth";

document.getElementById("formLogin").addEventListener("submit", async (e) => {

    e.preventDefault();

    const usuario = document.getElementById("usuario").value;
    const contrasena = document.getElementById("contrasena").value;

    const body = {
        usuario: usuario,
        contrasena: contrasena
    };

    try {

        const res = await fetch(`${API_AUTH}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            throw new Error(await res.text());
        }

        const data = await res.json();

        

        
        localStorage.setItem("usuario", JSON.stringify(data));

        
        const rol = data.rol.rolName;

        if (rol === "CLIENTE") {

            window.location.href = "index.html";

        } else {

            window.location.href = "index.html";

        }

    } catch (error) {

        alert(error.message);

    }

});