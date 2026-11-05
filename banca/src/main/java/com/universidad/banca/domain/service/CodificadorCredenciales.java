package com.universidad.banca.domain.service;

public interface CodificadorCredenciales {
    
    public String codificar(String plano);
    
    public boolean coincide(String plano, String codificado);

}
