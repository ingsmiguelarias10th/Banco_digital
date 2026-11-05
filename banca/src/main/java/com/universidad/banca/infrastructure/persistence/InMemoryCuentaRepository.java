// infrastructure/persistence/InMemoryCuentaRepository.java
package com.universidad.banca.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.universidad.banca.domain.model.Cuenta;
import com.universidad.banca.domain.repository.CuentaRepository;

@Repository
public class InMemoryCuentaRepository implements CuentaRepository {
    private final List<Cuenta> memoria = new ArrayList<>();

    public InMemoryCuentaRepository() {
        // Cuentas de prueba, una por cliente. Los PIN se guardan codificados con BCrypt (RNF03).
        // Cliente 1 -> PIN 1234, cliente 2 -> PIN 5678
        memoria.add(new Cuenta(1L, 1L, 2_000_000,
                "$2a$10$IHX1BbX8gEkxx3SObettC.o759/660gOH/Q.BPqpsDQYnroMwbgb6", 0, null));
        memoria.add(new Cuenta(2L, 2L, 500_000,
                "$2a$10$aBHVA81Od0h9E5wiREH3yuhdJt1jFJr0jU6rKmiCVwKBdYTW/5GBu", 0, null));
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        memoria.removeIf(c -> c.getId().equals(cuenta.getId())); // Reemplaza si ya existe (actualizar)
        memoria.add(cuenta);
        return cuenta;
    }

    @Override
    public Optional<Cuenta> buscarPorClienteId(Long clienteId) {
        return memoria.stream().filter(c -> c.getClienteId().equals(clienteId)).findFirst();
    }
}