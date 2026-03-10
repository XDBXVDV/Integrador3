const API_AUTH = "http://localhost:8080/auth";
const API_CLIENTES = "http://localhost:8080/cliente";


// LOGIN

document.getElementById("formLogin").addEventListener("submit", async (e) => {

    e.preventDefault();

    const usuario = document.getElementById("usuarioLogin").value;
    const contrasena = document.getElementById("contrasenaLogin").value;

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

        window.location.href = "index.html";

    } catch (error) {

        alert(error.message);

    }

});



// REGISTRO CLIENTE


document.getElementById("formRegistro").addEventListener("submit", async (e) => {

e.preventDefault();

const body = {

usuario: document.getElementById("usuarioRegistro").value,
email: document.getElementById("email").value,
contrasena: document.getElementById("contrasena").value,
idRol: 1,

nombre: document.getElementById("nombre").value,
apellido: document.getElementById("apellido").value,
dni: document.getElementById("dni").value,
telefono: document.getElementById("telefono").value,
direccion: document.getElementById("direccion").value

};

try{

const res = await fetch(`${API_CLIENTES}/crear`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify(body)

});

if(!res.ok){
throw new Error(await res.text());
}

alert("Cuenta creada correctamente");

document.getElementById("formRegistro").reset();

const modal = bootstrap.Modal.getInstance(document.getElementById("modalRegistro"));
modal.hide();

}catch(error){

alert(error.message);

}

});