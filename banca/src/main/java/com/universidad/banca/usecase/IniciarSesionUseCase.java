package com.universidad.banca.usecase;

import com.universidad.banca.domain.model.Cliente;
import com.universidad.banca.domain.repository.ClienteRepository;
import com.universidad.banca.domain.service.CodificadorCredenciales;

public class IniciarSesionUseCase {

    private ClienteRepository clienteRepository;
    private CodificadorCredenciales codificador;


    public IniciarSesionUseCase(ClienteRepository clienteRepository, CodificadorCredenciales codificador) {
        this.clienteRepository = clienteRepository;
        this.codificador = codificador;
    }

    public Cliente ejecutar(String usuario, String clave) {
        Cliente cliente = clienteRepository.buscarPorUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario o clave incorrectos"));
        if (!codificador.coincide(clave, cliente.getClave())) {
            throw new IllegalArgumentException("Usuario o clave incorrectos");                    
        }
        return cliente;                                                                           
    }
}
