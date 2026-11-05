package com.universidad.banca.usecase;

import com.universidad.banca.domain.model.Cuenta;
import com.universidad.banca.domain.repository.CuentaRepository;

public class ConsultarSaldoUseCase {

    private final CuentaRepository cuentaRepository;

    public ConsultarSaldoUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public long ejecutar(Long clienteId) {
        Cuenta cuenta = cuentaRepository.buscarPorClienteId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return cuenta.getSaldo();
    }
}