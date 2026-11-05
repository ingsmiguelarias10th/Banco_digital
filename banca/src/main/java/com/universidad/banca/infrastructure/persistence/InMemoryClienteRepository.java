package com.universidad.banca.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.universidad.banca.domain.model.Cliente;
import com.universidad.banca.domain.repository.ClienteRepository;

@Repository
public class InMemoryClienteRepository implements ClienteRepository {
    private final List<Cliente> memoria = new ArrayList<>();

    public InMemoryClienteRepository() {
        // Clientes de prueba. Las claves se guardan codificadas con BCrypt (RNF03).
        // jperez -> Clave123, mgomez -> Clave456
        memoria.add(new Cliente(1L, "jperez",
                "$2a$10$WFgz9A1NV0BxIZWokZc/kuH3s5c/ZZvFgDKIg9zpq5/5OF9yM25Pe",
                "jperez@correo.com", "3001234567", "Calle 10 # 20-30"));
        memoria.add(new Cliente(2L, "mgomez",
                "$2a$10$jy4t2U6I65W4fc67qdoJyeBWvq22mL.FZsSzGr8t/6QLZtYvuztfG",
                "mgomez@correo.com", "3109876543", "Carrera 5 # 12-40"));
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        memoria.removeIf(c -> c.getId().equals(cliente.getId())); // Reemplaza si ya existe (actualizar)
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