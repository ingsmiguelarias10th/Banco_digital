package com.universidad.banca.domain.service;

public interface CodificadorCredenciales {

    String codificar(String credencial);
    boolean coincide(String textoPlano, String codificado);

}
