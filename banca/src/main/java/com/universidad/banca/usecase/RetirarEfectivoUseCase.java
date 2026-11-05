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

    public Map<Integer, Integer> ejecutar(Long clienteId, long monto, String pin) {
        LocalDate hoy = LocalDate.now();

        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorClienteId(clienteId);
        if (cuentaOpt.isEmpty()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }
        Cuenta cuenta = cuentaOpt.get();

        validador.validar(cuenta, monto, pin, hoy);
        Map<Integer, Integer> billetes = dispensador.calcular(monto);

    // Si el acumulado es de otro día (o nunca hubo retiros), se reinicia (C7)
        if (!hoy.equals(cuenta.getFechaAcumulado())) {
            cuenta.setAcumuladoDiario(0L);
            cuenta.setFechaAcumulado(hoy);
        }

        cuenta.setSaldo(cuenta.getSaldo() - monto);
        cuenta.setAcumuladoDiario(cuenta.getAcumuladoDiario() + monto);
        cuentaRepository.guardar(cuenta);
        return billetes;
        }
        
    }
