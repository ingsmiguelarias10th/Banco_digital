package com.universidad.banca.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universidad.banca.domain.repository.ClienteRepository;
import com.universidad.banca.domain.repository.CuentaRepository;
import com.universidad.banca.domain.service.CodificadorCredenciales;
import com.universidad.banca.domain.service.DispensadorBilletes;
import com.universidad.banca.domain.service.ValidadorRetiro;
import com.universidad.banca.usecase.ActualizarDatosUseCase;
import com.universidad.banca.usecase.ConsultarSaldoUseCase;
import com.universidad.banca.usecase.DepositarFondosUseCase;
import com.universidad.banca.usecase.IniciarSesionUseCase;
import com.universidad.banca.usecase.ModificarCredencialesUseCase;
import com.universidad.banca.usecase.RetirarEfectivoUseCase;

@Configuration
public class BeanConfiguration {

    // ===== Incremento 1: inicio de sesión y consulta de saldo (HU-01, HU-02) =====

    @Bean
    public IniciarSesionUseCase iniciarSesionUseCase(ClienteRepository repo, CodificadorCredenciales codificador) {
        return new IniciarSesionUseCase(repo, codificador);
    }

    @Bean
    public ConsultarSaldoUseCase consultarSaldoUseCase(CuentaRepository repo) {
        return new ConsultarSaldoUseCase(repo);
    }

    // ===== Incremento 2: depósitos y retiros (HU-03, HU-06) =====

    @Bean
    public DepositarFondosUseCase depositarFondosUseCase(CuentaRepository repo) {
        return new DepositarFondosUseCase(repo);
    }

    @Bean
    public ValidadorRetiro validadorRetiro(CodificadorCredenciales codificador) {
        return new ValidadorRetiro(codificador);
    }

    @Bean
    public DispensadorBilletes dispensadorBilletes() {
        return new DispensadorBilletes();
    }

    @Bean
    public RetirarEfectivoUseCase retirarEfectivoUseCase(CuentaRepository repo, ValidadorRetiro validador, DispensadorBilletes dispensador) {
        return new RetirarEfectivoUseCase(repo, validador, dispensador);
    }

    // ===== Incremento 3: datos personales y credenciales (HU-04, HU-05) =====

    @Bean
    public ActualizarDatosUseCase actualizarDatosUseCase(ClienteRepository clienteRepository) {
        return new ActualizarDatosUseCase(clienteRepository);
    }

    @Bean
    public ModificarCredencialesUseCase modificarCredencialesUseCase(
            ClienteRepository clienteRepository,
            CuentaRepository cuentaRepository,
            CodificadorCredenciales codificador) {
        return new ModificarCredencialesUseCase(clienteRepository, cuentaRepository, codificador);
    }
}