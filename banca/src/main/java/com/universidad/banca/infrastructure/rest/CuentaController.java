package com.universidad.banca.infrastructure.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.banca.usecase.ConsultarSaldoUseCase;
import com.universidad.banca.usecase.DepositarFondosUseCase;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final ConsultarSaldoUseCase consultarSaldoUseCase;
    private final DepositarFondosUseCase depositarFondosUseCase;

    public CuentaController(ConsultarSaldoUseCase consultarSaldoUseCase,
                            DepositarFondosUseCase depositarFondosUseCase) {
        this.consultarSaldoUseCase = consultarSaldoUseCase;
        this.depositarFondosUseCase = depositarFondosUseCase;
    }

    @GetMapping("/{clienteId}/saldo")
    public ResponseEntity consultarSaldo(@PathVariable Long clienteId) {
        try {
            return ResponseEntity.ok(consultarSaldoUseCase.ejecutar(clienteId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{clienteId}/depositos")
    public ResponseEntity depositar(@PathVariable Long clienteId,
                                    @RequestBody DepositoRequest request) {
        try {
            return ResponseEntity.ok(depositarFondosUseCase.ejecutar(clienteId, request.getMonto()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class DepositoRequest {

        private long monto;

        public long getMonto() {
            return monto;
        }

        public void setMonto(long monto) {
            this.monto = monto;
        }
    }
}