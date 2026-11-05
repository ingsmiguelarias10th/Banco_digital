package com.universidad.banca.domain.repository;

import java.util.Optional;

import com.universidad.banca.domain.model.Cuenta;

public interface CuentaRepository {
    Cuenta guardar(Cuenta cuenta);
    Optional<Cuenta> buscarPorClienteId(Long id);
}
