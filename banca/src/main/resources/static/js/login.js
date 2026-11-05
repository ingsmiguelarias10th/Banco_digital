// ===== HU-01: Inicio de sesión =====

// Si ya hay una sesión abierta, se va directo al panel
if (obtenerSesion()) {
    window.location.href = "panel.html";
}

const formLogin = document.getElementById("form-login");
const mensajeLogin = document.getElementById("mensaje-login");
const botonLogin = document.getElementById("boton-login");

formLogin.addEventListener("submit", async function (evento) {
    evento.preventDefault();

    const usuario = document.getElementById("usuario").value.trim();
    const clave = document.getElementById("clave").value;

    if (!usuario || !clave) {
        mostrarMensaje(mensajeLogin, "Ingrese su usuario y su clave de acceso.", "error");
        return;
    }

    botonLogin.disabled = true;
    mostrarMensaje(mensajeLogin, "", null);

    // POST /api/auth/login
    const resultado = await llamarApi("POST", "/api/auth/login", { usuario: usuario, clave: clave });

    botonLogin.disabled = false;

    if (resultado.ok) {
        // C1: se inicia sesión y se muestra el panel
        guardarSesion(resultado.datos.clienteId, resultado.datos.usuario);
        window.location.href = "panel.html";
    } else {
        // C2 y C3: "Usuario o clave incorrectos"
        mostrarMensaje(mensajeLogin, resultado.datos.mensaje, "error");
        document.getElementById("clave").value = "";
    }
});
