package com.universidad.banca.domain.service;

import java.util.HashMap;
import java.util.Map;

public class DispensadorBilletes {

    private final int[] DENOMINACIONES = {100000, 50000, 20000, 10000};

    public Map<Integer, Integer> calcular(long monto) {

        Map<Integer, Integer> resultado = new HashMap<>();
        long resto = monto;

        for (int denominacion : DENOMINACIONES) {
            
            if (resto >= denominacion) {
                
                int cantidad = (int) (resto / denominacion);
                
                resultado.put(denominacion, cantidad);
                
                resto = resto % denominacion;

            }

        }

        return resultado;
        
    }
}