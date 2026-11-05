package com.universidad.banca.usecase;

import java.util.Optional;

import com.universidad.banca.domain.model.Cuenta;
import com.universidad.banca.domain.repository.CuentaRepository;

public class DepositarFondosUseCase {
    private final CuentaRepository cuentaRepository;

    public DepositarFondosUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Cuenta ejecutar(Long clienteId, long monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");   // C2
        }

        Optional<Cuenta> resultado = cuentaRepository.buscarPorClienteId(clienteId);
        if (resultado.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        Cuenta cuenta = resultado.get();
        cuenta.setSaldo(cuenta.getSaldo() + monto);                                    // C1
        return cuentaRepository.guardar(cuenta);
    }
}