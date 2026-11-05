package com.universidad.banca.usecase;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.universidad.banca.domain.model.Cuenta;
import com.universidad.banca.domain.repository.CuentaRepository;
import com.universidad.banca.domain.service.DispensadorBilletes;
import com.universidad.banca.domain.service.ValidadorRetiro;


public class RetirarEfectivoUseCase {

    private final CuentaRepository cuentaRepository;
    private final ValidadorRetiro validador;
    private final DispensadorBilletes dispensador;

    public RetirarEfectivoUseCase(CuentaRepository cuentaRepository, ValidadorRetiro validador, DispensadorBilletes dispensador) {
        this.cuentaRepository = cuentaRepository;
        this.validador = validador;
        this.dispensador = dispensador;
    }

    public Map<Integer, Integer> ejecutar(Long clientId, long monto, String pin) {
        
        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorClienteId(clientId);
        
        if (cuentaOpt.isEmpty()) {
            throw new IllegalArgumentException("Error: La cuenta con ID " + clientId + " no existe.");
        }
        
        Cuenta cuenta = cuentaOpt.get();

        validador.validar(cuenta, monto, pin, LocalDate.now());

        cuenta.setSaldo(cuenta.getSaldo() - monto);

        if (cuenta.getFechaAcumulado() == null || !cuenta.getFechaAcumulado().equals(LocalDate.now())) {
            cuenta.setAcumuladoDiario(0l);
            cuenta.setFechaAcumulado(LocalDate.now());
        }
        
        cuenta.setAcumuladoDiario(cuenta.getAcumuladoDiario() + monto);

        cuentaRepository.guardar(cuenta);

        return dispensador.calcular(monto);
        
    }
}