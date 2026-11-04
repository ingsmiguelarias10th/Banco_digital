package com.universidad.banca.domain.repository;

import com.universidad.banca.domain.Cuenta;
import java.util.Optional;

public interface CuentaRepository {
    Cuenta guardar(Cuenta cuenta);
    Optional<Cuenta> buscarPorClienteId(Long id);
}
