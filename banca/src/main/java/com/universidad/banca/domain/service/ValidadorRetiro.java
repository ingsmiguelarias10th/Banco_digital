package com.universidad.banca.domain.service;

import java.time.LocalDate;

import com.universidad.banca.domain.model.Cuenta;

public class ValidadorRetiro {
    
    public static final Long MONTO_MINIMO = 10000L;
    public static final Long TOPE_DIARIO = 1000000L;

    private CodificadorCredenciales codificador;

    public ValidadorRetiro(CodificadorCredenciales codificador) {
        this.codificador = codificador;
    }

public void validar(Cuenta cuenta, Long monto, String pin, LocalDate fecha) {

        if (!codificador.coincide(pin, cuenta.getPin())) {
            throw new IllegalArgumentException("Error: El PIN ingresado es incorrecto.");
        }

        if (monto < MONTO_MINIMO) {
            throw new IllegalArgumentException("Error: El monto mínimo de retiro es " + MONTO_MINIMO + ".");
        }

        if (monto % 10000 != 0) {
            throw new IllegalArgumentException("Error: El monto debe poder entregarse con billetes de 100.000, 50.000, 20.000 y 10.000.");
        }

        long acumuladoHoy = 0;
        if (cuenta.getFechaAcumulado() != null && cuenta.getFechaAcumulado().equals(fecha)) {
            acumuladoHoy = cuenta.getAcumuladoDiario();
        } 
        
        if (acumuladoHoy + monto > TOPE_DIARIO) {
            throw new IllegalArgumentException("Error: El retiro excede el tope diario de " + TOPE_DIARIO + ".");
        }

        if (monto > cuenta.getSaldo()) {
            throw new IllegalArgumentException("Error: Saldo insuficiente en la cuenta.");
        }
        
    }
    
}
