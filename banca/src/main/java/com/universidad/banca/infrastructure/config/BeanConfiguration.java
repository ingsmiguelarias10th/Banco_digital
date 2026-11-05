
package com.universidad.banca.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universidad.banca.domain.repository.ClienteRepository;
import com.universidad.banca.domain.repository.CuentaRepository;
import com.universidad.banca.domain.service.CodificadorCredenciales;
import com.universidad.banca.usecase.ConsultarSaldoUseCase;
import com.universidad.banca.usecase.DepositarFondosUseCase;
import com.universidad.banca.usecase.IniciarSesionUseCase;

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

    // ===== Incremento 3: datos personales y credenciales (HU-04, HU-05) =====

}