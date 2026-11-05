package com.universidad.banca.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universidad.banca.domain.repository.ClienteRepository;
import com.universidad.banca.domain.repository.CuentaRepository;
import com.universidad.banca.domain.service.CodificadorCredenciales;
import com.universidad.banca.usecase.ActualizarDatosUseCase;
import com.universidad.banca.usecase.ModificarCredencialesUseCase;

@Configuration
public class BeanConfiguration {

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