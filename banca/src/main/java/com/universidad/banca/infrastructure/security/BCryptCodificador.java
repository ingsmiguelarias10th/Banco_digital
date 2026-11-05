package com.universidad.banca.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.universidad.banca.domain.service.CodificadorCredenciales;

public class BCryptCodificador implements CodificadorCredenciales{

    private BCryptPasswordEncoder encoder;

    public BCryptCodificador() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String codificar(String plano) {

        return encoder.encode(plano);

    }

    @Override
    public boolean coincide(String plano, String codificado) {

        return encoder.matches(plano, codificado);

    }

}