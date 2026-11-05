package com.universidad.banca.infrastructure.persistence;

import com.universidad.banca.domain.model.Cliente;
import com.universidad.banca.domain.repository.ClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryClienteRepository implements ClienteRepository {

    private final List<Cliente> memoria = new ArrayList<>();

    public InMemoryClienteRepository() {
        memoria.add(new Cliente(1L, "cliente1", "admin123", "cliente@upb.edu.co", "3001234567", "Cra 9 # 45-20"));
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        memoria.removeIf(c -> c.getId().equals(cliente.getId()));
        memoria.add(cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return memoria.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Cliente> buscarPorUsuario(String usuario) {
        return memoria.stream().filter(c -> c.getUsuario().equals(usuario)).findFirst();
    }
}