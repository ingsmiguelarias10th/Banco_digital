// ===== Funciones comunes del frontend =====

// Si la página se abre como archivo (file://), las peticiones van al servidor local.
// Si la sirve Spring Boot (http://localhost:8080), se usa la misma dirección.
const API = window.location.protocol === "file:" ? "http://localhost:8080" : "";

/**
 * Llama a la API REST y devuelve { ok, status, datos }.
 * Si el servidor responde con error, datos.mensaje trae el motivo (RF11).
 */
async function llamarApi(metodo, ruta, cuerpo) {
    const opciones = { method: metodo, headers: {} };
    if (cuerpo !== undefined) {
        opciones.headers["Content-Type"] = "application/json";
        opciones.body = JSON.stringify(cuerpo);
    }

    let respuesta;
    try {
        respuesta = await fetch(API + ruta, opciones);
    } catch (error) {
        return {
            ok: false,
            status: 0,
            datos: { mensaje: "No se pudo conectar con el servidor. Verifique que la aplicación esté en ejecución." }
        };
    }

    let datos = {};
    try {
        datos = await respuesta.json();
    } catch (error) {
        datos = {};
    }

    if (!respuesta.ok && !datos.mensaje) {
        datos.mensaje = "Error " + respuesta.status + " al procesar la solicitud.";
    }
    return { ok: respuesta.ok, status: respuesta.status, datos: datos };
}

/** Formatea un número como pesos colombianos: 2000000 -> "$ 2.000.000" */
function formatoPesos(valor) {
    return "$ " + new Intl.NumberFormat("es-CO").format(valor);
}

/** Deja solo los dígitos de un texto: "1.250.000" -> "1250000" */
function soloDigitos(texto) {
    return (texto || "").replace(/\D/g, "");
}

/** Muestra un mensaje de éxito o error en el elemento indicado */
function mostrarMensaje(elemento, texto, tipo) {
    elemento.textContent = texto;
    elemento.classList.remove("exito", "error");
    if (tipo) {
        elemento.classList.add(tipo);
    }
}

// ===== Sesión del cliente (se guarda en el navegador tras el login) =====

function guardarSesion(clienteId, usuario) {
    sessionStorage.setItem("clienteId", clienteId);
    sessionStorage.setItem("usuario", usuario);
}

function obtenerSesion() {
    const clienteId = sessionStorage.getItem("clienteId");
    const usuario = sessionStorage.getItem("usuario");
    if (!clienteId) {
        return null;
    }
    return { clienteId: Number(clienteId), usuario: usuario };
}

function cerrarSesion() {
    sessionStorage.clear();
    window.location.href = "index.html";
}
