package com.universidad.banca.domain.repository;

import java.util.Optional;

import com.universidad.banca.domain.model.Cliente;

public interface ClienteRepository {

    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorUsuario(String usuario);

}
