package com.universidad.banca.usecase;

import com.universidad.banca.domain.model.Cliente;
import com.universidad.banca.domain.model.Cuenta;
import com.universidad.banca.domain.model.TipoCredencial;
import com.universidad.banca.domain.repository.ClienteRepository;
import com.universidad.banca.domain.repository.CuentaRepository;
import com.universidad.banca.domain.service.CodificadorCredenciales;

public class ModificarCredencialesUseCase {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final CodificadorCredenciales codificador;

    public ModificarCredencialesUseCase(ClienteRepository clienteRepository,
                                        CuentaRepository cuentaRepository,
                                        CodificadorCredenciales codificador) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.codificador = codificador;
    }

    public void ejecutar(Long clienteId, TipoCredencial tipo, String actual, String nueva) {
        if (tipo == null) {
            throw new IllegalArgumentException("Debe especificar el tipo de credencial a modificar (CLAVE o PIN).");
        }
        if (actual == null || actual.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar la credencial actual.");
        }
        if (nueva == null || nueva.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar la nueva credencial.");
        }

        if (tipo == TipoCredencial.PIN) {
            Cuenta cuenta = cuentaRepository.buscarPorClienteId(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró cuenta para el cliente: " + clienteId));

            // C4: el PIN actual debe coincidir con el registrado
            if (!codificador.coincide(actual, cuenta.getPin())) {
                throw new IllegalArgumentException("El PIN actual ingresado no coincide con el registrado en el sistema.");
            }

            // C2: el nuevo PIN debe tener exactamente cuatro dígitos numéricos
            if (!nueva.matches("^\\d{4}$")) {
                throw new IllegalArgumentException("El nuevo PIN debe contener exactamente cuatro dígitos numéricos.");
            }

            // C1: se guarda el nuevo PIN codificado (RNF03)
            cuenta.setPin(codificador.codificar(nueva));
            cuentaRepository.guardar(cuenta);

        } else if (tipo == TipoCredencial.CLAVE) {
            Cliente cliente = clienteRepository.buscarPorId(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el cliente con ID: " + clienteId));

            // C4: la clave actual debe coincidir con la registrada
            if (!codificador.coincide(actual, cliente.getClave())) {
                throw new IllegalArgumentException("La clave de acceso actual no coincide con la registrada en el sistema.");
            }

            // C3: se guarda la nueva clave codificada (RNF03)
            cliente.setClave(codificador.codificar(nueva));
            clienteRepository.guardar(cliente);
        }
    }
}