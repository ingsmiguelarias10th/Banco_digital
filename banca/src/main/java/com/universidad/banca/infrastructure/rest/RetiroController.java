package com.universidad.banca.infrastructure.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.banca.usecase.RetirarEfectivoUseCase;

@RestController
@RequestMapping("/api/retiros")
@CrossOrigin(origins = "*")
public class RetiroController {

    private final RetirarEfectivoUseCase retirarEfectivoUseCase;

    public RetiroController(RetirarEfectivoUseCase retirarEfectivoUseCase) {
        this.retirarEfectivoUseCase = retirarEfectivoUseCase;
    }

    // POST /api/retiros
    @PostMapping
    public ResponseEntity<?> retirar(@RequestBody RetiroRequest request) {
        try {
            Map<Integer, Integer> billetes = retirarEfectivoUseCase.ejecutar(
                    request.getClienteId(), request.getMonto(), request.getPin());
            return ResponseEntity.ok(Map.of("mensaje", "Retiro exitoso", "billetes", billetes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    // DTO auxiliar interno para recibir los datos JSON
    public static class RetiroRequest {
        private Long clienteId;
        private long monto;
        private String pin;

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

        public long getMonto() { return monto; }
        public void setMonto(long monto) { this.monto = monto; }

        public String getPin() { return pin; }
        public void setPin(String pin) { this.pin = pin; }
    }
}