package com.universidad.banca.domain.model;

import java.time.LocalDate;

public class Cuenta {
    
    private Long id;
    private Long clienteId;
    private Long saldo;
    private String pin;
    private Long acumuladoDiario;
    private LocalDate fechaAcumulado;

    public Cuenta(Long id, Long clienteId, long saldo, String pin, long acumuladoDiario, LocalDate fechaAcumulado) {
        this.id = id;
        this.clienteId = clienteId;
        this.saldo = saldo;
        this.pin = pin;
        this.acumuladoDiario = acumuladoDiario;
        this.fechaAcumulado = fechaAcumulado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Long getAcumuladoDiario() {
        return acumuladoDiario;
    }

    public void setAcumuladoDiario(Long acumuladoDiario) {
        this.acumuladoDiario = acumuladoDiario;
    }

    public LocalDate getFechaAcumulado() {
        return fechaAcumulado;
    }

    public void setFechaAcumulado(LocalDate fechaAcumulado) {
        this.fechaAcumulado = fechaAcumulado;
    }

}
