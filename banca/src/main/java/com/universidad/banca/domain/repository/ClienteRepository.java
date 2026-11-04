package com.universidad.banca.domain.repository;

import com.universidad.banca.domain.Cliente;
import java.util.Optional;

public interface ClienteRepository {

    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorUsuario(String usuario);

}
