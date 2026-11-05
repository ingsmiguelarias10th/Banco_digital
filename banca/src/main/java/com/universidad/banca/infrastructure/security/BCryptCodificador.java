package com.universidad.banca.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.universidad.banca.domain.service.CodificadorCredenciales;

@Component
public class BCryptCodificador implements CodificadorCredenciales {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String codificar(String textoPlano) {
        return encoder.encode(textoPlano);
    }

    @Override
    public boolean coincide(String textoPlano, String codificado) {
        return encoder.matches(textoPlano, codificado);
    }

}
