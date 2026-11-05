package com.universidad.banca.infrastructure.rest;

import com.universidad.banca.domain.model.Cliente;
import com.universidad.banca.domain.model.TipoCredencial;
import com.universidad.banca.usecase.ActualizarDatosUseCase;
import com.universidad.banca.usecase.ModificarCredencialesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ActualizarDatosUseCase actualizarDatosUseCase;
    private final ModificarCredencialesUseCase modificarCredencialesUseCase;

    public ClienteController(ActualizarDatosUseCase actualizarDatosUseCase,
                             ModificarCredencialesUseCase modificarCredencialesUseCase) {
        this.actualizarDatosUseCase = actualizarDatosUseCase;
        this.modificarCredencialesUseCase = modificarCredencialesUseCase;
    }

    // HU-04: Endpoint para Actualización de Datos Personales
    @PutMapping("/{clienteId}/datos")
    public ResponseEntity<Object> actualizarDatos(@PathVariable Long clienteId,
                                                  @RequestBody DatosRequest request) {
        try {
            Cliente actualizado = actualizarDatosUseCase.ejecutar(
                    clienteId,
                    request.getCorreo(),
                    request.getTelefono(),
                    request.getDireccion()
            );
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("mensaje", "Datos personales actualizados exitosamente.");
            respuesta.put("id", actualizado.getId());
            respuesta.put("usuario", actualizado.getUsuario());
            respuesta.put("correo", actualizado.getCorreo());
            respuesta.put("telefono", actualizado.getTelefono());
            respuesta.put("direccion", actualizado.getDireccion());
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    // HU-05: Endpoint para Modificación de Credenciales (CLAVE o PIN)
    @PutMapping("/{clienteId}/credenciales")
    public ResponseEntity<Object> modificarCredenciales(@PathVariable Long clienteId,
                                                        @RequestBody CredencialRequest request) {
        try {
            modificarCredencialesUseCase.ejecutar(
                    clienteId,
                    request.getTipo(),
                    request.getActual(),
                    request.getNueva()
            );
            String detalle = (request.getTipo() == TipoCredencial.PIN)
                    ? "Cambio de PIN de seguridad exitoso."
                    : "Cambio de clave de acceso exitoso.";
            return ResponseEntity.ok(Map.of("mensaje", detalle));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    // Clase interna estática para HU-04 según el diagrama de clases
    public static class DatosRequest {
        private String correo;
        private String telefono;
        private String direccion;

        public DatosRequest() {
        }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
    }

    // Clase interna estática para HU-05 según el diagrama de clases
    public static class CredencialRequest {
        private TipoCredencial tipo;
        private String actual;
        private String nueva;

        public CredencialRequest() {
        }

        public TipoCredencial getTipo() { return tipo; }
        public void setTipo(TipoCredencial tipo) { this.tipo = tipo; }

        public String getActual() { return actual; }
        public void setActual(String actual) { this.actual = actual; }

        public String getNueva() { return nueva; }
        public void setNueva(String nueva) { this.nueva = nueva; }
    }
}