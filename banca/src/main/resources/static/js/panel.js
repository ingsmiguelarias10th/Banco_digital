// ===== Panel interactivo =====

const sesion = obtenerSesion();
if (!sesion) {
    // Sin sesión no se puede entrar al panel
    window.location.href = "index.html";
}

const clienteId = sesion ? sesion.clienteId : null;

// ----- Datos de la barra lateral y la tarjeta -----
if (sesion) {
    document.getElementById("avatar").textContent = sesion.usuario.charAt(0);
    document.getElementById("sidebar-usuario").textContent = sesion.usuario;
    document.getElementById("tarjeta-titular").textContent = sesion.usuario;
    document.getElementById("tarjeta-numero").textContent = String(clienteId).padStart(4, "0");
}
document.getElementById("boton-salir").addEventListener("click", cerrarSesion);

// ===== HU-02: Consulta de saldo =====

function mostrarSaldo(texto) {
    document.querySelectorAll(".saldo-actual").forEach(function (elemento) {
        elemento.textContent = texto;
    });
}

async function cargarSaldo() {
    // GET /api/cuentas/{clienteId}/saldo
    const resultado = await llamarApi("GET", "/api/cuentas/" + clienteId + "/saldo");
    if (resultado.ok) {
        mostrarSaldo(formatoPesos(resultado.datos.saldo));
    } else {
        mostrarSaldo("No disponible");
    }
}

// ===== Navegación entre secciones =====

const vistas = {
    inicio: { titulo: "Inicio", subtitulo: "Resumen de su cuenta" },
    operaciones: { titulo: "Operaciones", subtitulo: "Deposite o retire dinero de su cuenta" },
    perfil: { titulo: "Datos personales", subtitulo: "Consulte y actualice su información de contacto" },
    seguridad: { titulo: "Seguridad", subtitulo: "Cambie su clave de acceso o su PIN de seguridad" }
};
const botonesNav = document.querySelectorAll(".nav-boton");

function mostrarVista(nombre) {
    Object.keys(vistas).forEach(function (vista) {
        document.getElementById("vista-" + vista).classList.toggle("oculto", vista !== nombre);
    });
    botonesNav.forEach(function (boton) {
        boton.classList.toggle("activo", boton.dataset.vista === nombre);
    });
    document.getElementById("titulo-vista").textContent = vistas[nombre].titulo;
    document.getElementById("subtitulo-vista").textContent = vistas[nombre].subtitulo;
    window.scrollTo(0, 0);
}

botonesNav.forEach(function (boton) {
    boton.addEventListener("click", function () {
        mostrarVista(boton.dataset.vista);
    });
});

// Accesos rápidos del inicio: abren Operaciones y ponen el cursor en el monto
document.querySelectorAll(".acceso").forEach(function (boton) {
    boton.addEventListener("click", function () {
        mostrarVista("operaciones");
        document.getElementById(boton.dataset.ir).focus();
    });
});

// ===== Movimientos de la sesión (solo se muestran en el navegador) =====

const listaMovimientos = document.getElementById("lista-movimientos");

function obtenerMovimientos() {
    try {
        return JSON.parse(sessionStorage.getItem("movimientos")) || [];
    } catch (error) {
        return [];
    }
}

function registrarMovimiento(tipo, monto) {
    const movimientos = obtenerMovimientos();
    movimientos.unshift({
        tipo: tipo,
        monto: monto,
        hora: new Date().toLocaleTimeString("es-CO", { hour: "2-digit", minute: "2-digit" })
    });
    sessionStorage.setItem("movimientos", JSON.stringify(movimientos));
    pintarMovimientos();
}

function pintarMovimientos() {
    const movimientos = obtenerMovimientos();
    listaMovimientos.innerHTML = "";

    if (movimientos.length === 0) {
        const vacio = document.createElement("p");
        vacio.className = "vacio";
        vacio.textContent = "Aún no hay movimientos en esta sesión.";
        listaMovimientos.appendChild(vacio);
        return;
    }

    movimientos.forEach(function (movimiento) {
        const esDeposito = movimiento.tipo === "deposito";
        const fila = document.createElement("div");
        fila.className = "movimiento";

        const datos = document.createElement("div");
        const nombre = document.createElement("p");
        nombre.className = "movimiento-nombre";
        nombre.textContent = esDeposito ? "Depósito" : "Retiro";
        const hora = document.createElement("p");
        hora.className = "movimiento-hora";
        hora.textContent = movimiento.hora;
        datos.appendChild(nombre);
        datos.appendChild(hora);

        const monto = document.createElement("p");
        monto.className = "movimiento-monto" + (esDeposito ? " positivo" : "");
        monto.textContent = (esDeposito ? "+ " : "- ") + formatoPesos(movimiento.monto);

        fila.appendChild(datos);
        fila.appendChild(monto);
        listaMovimientos.appendChild(fila);
    });
}

// ===== Campos de monto: solo dígitos, con separador de miles =====

function ajustarAncho(input) {
    // El campo crece con el número para que el signo $ quede junto a la cifra
    input.style.width = (Math.max(1, input.value.length) + 0.6) + "ch";
}

function prepararCampoMonto(input) {
    input.addEventListener("input", function () {
        const digitos = soloDigitos(input.value);
        input.value = digitos ? new Intl.NumberFormat("es-CO").format(Number(digitos)) : "";
        ajustarAncho(input);
    });
}

function leerMonto(input) {
    const digitos = soloDigitos(input.value);
    return digitos ? Number(digitos) : null;
}

function limpiarCampoMonto(input) {
    input.value = "";
    ajustarAncho(input);
}

// ===== HU-03: Depósito =====

const formDeposito = document.getElementById("form-deposito");
const montoDeposito = document.getElementById("monto-deposito");
const mensajeDeposito = document.getElementById("mensaje-deposito");
prepararCampoMonto(montoDeposito);

formDeposito.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    const monto = leerMonto(montoDeposito);

    if (monto === null) {
        mostrarMensaje(mensajeDeposito, "Ingrese el monto a depositar.", "error");
        return;
    }

    const boton = formDeposito.querySelector("button[type=submit]");
    boton.disabled = true;

    // POST /api/cuentas/{clienteId}/depositos
    const resultado = await llamarApi("POST", "/api/cuentas/" + clienteId + "/depositos", { monto: monto });

    boton.disabled = false;

    if (resultado.ok) {
        // C1: se suma al saldo y se notifica
        mostrarMensaje(mensajeDeposito, resultado.datos.mensaje + ". Nuevo saldo: " + formatoPesos(resultado.datos.saldo), "exito");
        mostrarSaldo(formatoPesos(resultado.datos.saldo));
        registrarMovimiento("deposito", monto);
        limpiarCampoMonto(montoDeposito);
    } else {
        // C2: monto menor o igual a cero
        mostrarMensaje(mensajeDeposito, resultado.datos.mensaje, "error");
    }
});

// ===== HU-06: Retiro =====

const formRetiro = document.getElementById("form-retiro");
const montoRetiro = document.getElementById("monto-retiro");
const pinRetiro = document.getElementById("pin-retiro");
const mensajeRetiro = document.getElementById("mensaje-retiro");
const resultadoRetiro = document.getElementById("resultado-retiro");
prepararCampoMonto(montoRetiro);

document.querySelectorAll(".monto-rapido").forEach(function (boton) {
    boton.addEventListener("click", function () {
        montoRetiro.value = new Intl.NumberFormat("es-CO").format(Number(boton.dataset.monto));
        ajustarAncho(montoRetiro);
        pinRetiro.focus();
    });
});

function pintarBilletes(billetes) {
    resultadoRetiro.innerHTML = "";

    const titulo = document.createElement("span");
    titulo.className = "etiqueta-pequena";
    titulo.textContent = "Billetes entregados";
    resultadoRetiro.appendChild(titulo);

    // Se ordenan de mayor a menor denominación
    const denominaciones = Object.keys(billetes).map(Number).sort(function (a, b) { return b - a; });
    let totalBilletes = 0;

    denominaciones.forEach(function (denominacion) {
        const cantidad = billetes[denominacion];
        totalBilletes += cantidad;

        const fila = document.createElement("div");
        fila.className = "billete";

        const valor = document.createElement("span");
        valor.className = "billete-valor";
        const icono = document.createElement("span");
        icono.className = "billete-icono";
        icono.setAttribute("aria-hidden", "true");
        valor.appendChild(icono);
        valor.appendChild(document.createTextNode(formatoPesos(denominacion)));

        const unidades = document.createElement("span");
        unidades.className = "billete-cantidad";
        unidades.textContent = "x " + cantidad;

        fila.appendChild(valor);
        fila.appendChild(unidades);
        resultadoRetiro.appendChild(fila);
    });

    const total = document.createElement("div");
    total.className = "billetes-total";
    const textoTotal = document.createElement("span");
    textoTotal.textContent = "Total de billetes";
    const numeroTotal = document.createElement("span");
    numeroTotal.textContent = totalBilletes;
    total.appendChild(textoTotal);
    total.appendChild(numeroTotal);
    resultadoRetiro.appendChild(total);

    resultadoRetiro.classList.remove("oculto");
}

formRetiro.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    const monto = leerMonto(montoRetiro);
    const pin = pinRetiro.value;

    resultadoRetiro.classList.add("oculto");

    if (monto === null) {
        mostrarMensaje(mensajeRetiro, "Ingrese el monto a retirar.", "error");
        return;
    }
    if (!pin) {
        mostrarMensaje(mensajeRetiro, "Ingrese su PIN de seguridad.", "error");
        return;
    }

    const boton = formRetiro.querySelector("button[type=submit]");
    boton.disabled = true;

    // POST /api/retiros
    const resultado = await llamarApi("POST", "/api/retiros", { clienteId: clienteId, monto: monto, pin: pin });

    boton.disabled = false;
    pinRetiro.value = "";

    if (resultado.ok) {
        // C1: se entrega con la menor cantidad de billetes y se actualiza el saldo
        mostrarMensaje(mensajeRetiro, resultado.datos.mensaje + " por " + formatoPesos(monto) + ".", "exito");
        pintarBilletes(resultado.datos.billetes);
        registrarMovimiento("retiro", monto);
        limpiarCampoMonto(montoRetiro);
        cargarSaldo();
    } else {
        // C2 a C6: se indica el motivo del rechazo
        mostrarMensaje(mensajeRetiro, resultado.datos.mensaje, "error");
    }
});

// ===== HU-04: Datos personales =====

const formDatos = document.getElementById("form-datos");
const mensajeDatos = document.getElementById("mensaje-datos");
const datosActuales = document.getElementById("datos-actuales");

function pintarDatos(datos) {
    datosActuales.innerHTML = "";
    const filas = [
        ["Usuario", sesion ? sesion.usuario : ""],
        ["Correo", datos ? datos.correo : null],
        ["Teléfono", datos ? datos.telefono : null],
        ["Dirección", datos ? datos.direccion : null]
    ];
    filas.forEach(function (fila) {
        const div = document.createElement("div");
        div.className = "dato-fila";
        const nombre = document.createElement("span");
        nombre.textContent = fila[0];
        const valor = document.createElement("span");
        valor.textContent = fila[1] || "Sin cambios en esta sesión";
        div.appendChild(nombre);
        div.appendChild(valor);
        datosActuales.appendChild(div);
    });
}

function leerDatosGuardados() {
    try {
        return JSON.parse(sessionStorage.getItem("datos"));
    } catch (error) {
        return null;
    }
}

formDatos.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    const cuerpo = {
        correo: document.getElementById("correo").value.trim(),
        telefono: document.getElementById("telefono").value.trim(),
        direccion: document.getElementById("direccion").value.trim()
    };

    const boton = formDatos.querySelector("button[type=submit]");
    boton.disabled = true;

    // PUT /api/clientes/{clienteId}/datos
    const resultado = await llamarApi("PUT", "/api/clientes/" + clienteId + "/datos", cuerpo);

    boton.disabled = false;

    if (resultado.ok) {
        // C1: se actualizan los datos y se notifica
        mostrarMensaje(mensajeDatos, resultado.datos.mensaje, "exito");
        sessionStorage.setItem("datos", JSON.stringify(resultado.datos));
        pintarDatos(resultado.datos);
        formDatos.reset();
    } else {
        // C2: formato inválido, no se actualiza nada
        mostrarMensaje(mensajeDatos, resultado.datos.mensaje, "error");
    }
});

// ===== HU-05: Credenciales (clave y PIN) =====

function prepararFormularioCredencial(form) {
    const tipo = form.dataset.tipo;
    const actual = form.querySelector(".credencial-actual");
    const nueva = form.querySelector(".credencial-nueva");
    const confirmar = form.querySelector(".credencial-confirmar");
    const mensaje = form.querySelector(".mensaje");
    const boton = form.querySelector("button[type=submit]");

    form.addEventListener("submit", async function (evento) {
        evento.preventDefault();

        if (!actual.value || !nueva.value || !confirmar.value) {
            mostrarMensaje(mensaje, "Complete todos los campos.", "error");
            return;
        }
        if (nueva.value !== confirmar.value) {
            mostrarMensaje(mensaje, "La confirmación no coincide con la credencial nueva.", "error");
            return;
        }

        boton.disabled = true;

        // PUT /api/clientes/{clienteId}/credenciales
        const resultado = await llamarApi("PUT", "/api/clientes/" + clienteId + "/credenciales", {
            tipo: tipo,
            actual: actual.value,
            nueva: nueva.value
        });

        boton.disabled = false;

        if (resultado.ok) {
            // C1 y C3: se guarda la credencial nueva y se notifica
            mostrarMensaje(mensaje, resultado.datos.mensaje, "exito");
            form.reset();
        } else {
            // C2 y C4: formato inválido o credencial actual incorrecta
            mostrarMensaje(mensaje, resultado.datos.mensaje, "error");
        }
    });
}

prepararFormularioCredencial(document.getElementById("form-clave"));
prepararFormularioCredencial(document.getElementById("form-pin"));

// ===== Carga inicial =====
if (sesion) {
    cargarSaldo();
    pintarMovimientos();
    pintarDatos(leerDatosGuardados());
}
