package com.universidad.banca.infrastructure.persistence;

import com.universidad.banca.domain.model.Cuenta;
import com.universidad.banca.domain.repository.CuentaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryCuentaRepository implements CuentaRepository {

    private final List<Cuenta> memoria = new ArrayList<>();

    public InMemoryCuentaRepository() {
        memoria.add(new Cuenta(1L, 1L, "1234", 2500000L, 0L, LocalDate.now()));
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        memoria.removeIf(c -> c.getId().equals(cuenta.getId()));
        memoria.add(cuenta);
        return cuenta;
    }

    @Override
    public Optional<Cuenta> buscarPorClienteId(Long clienteId) {
        return memoria.stream().filter(c -> c.getClienteId().equals(clienteId)).findFirst();
    }
}