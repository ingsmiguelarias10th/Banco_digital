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

            // Criterio C4 (HU-05): Validar que el PIN actual coincida con el registrado
            boolean pinCoincide = codificador.coincide(actual, cuenta.getPin()) || actual.equals(cuenta.getPin());
            if (!pinCoincide) {
                throw new IllegalArgumentException("El PIN actual ingresado no coincide con el registrado en el sistema.");
            }

            // Criterio C2 (HU-05): Validar que el nuevo PIN tenga exactamente 4 dígitos numéricos
            if (!nueva.matches("^\\d{4}$")) {
                throw new IllegalArgumentException("El nuevo PIN debe contener exactamente cuatro dígitos numéricos.");
            }

            // Criterio C1 (HU-05): Guardar el nuevo PIN en el sistema
            cuenta.setPin(codificador.codificar(nueva));
            cuentaRepository.guardar(cuenta);

        } else if (tipo == TipoCredencial.CLAVE) {
            Cliente cliente = clienteRepository.buscarPorId(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el cliente con ID: " + clienteId));

            // Criterio C4 (HU-05): Validar que la clave actual coincida con la registrada
            boolean claveCoincide = codificador.coincide(actual, cliente.getClave()) || actual.equals(cliente.getClave());
            if (!claveCoincide) {
                throw new IllegalArgumentException("La clave de acceso actual no coincide con la registrada en el sistema.");
            }

            // Criterio C3 (HU-05): Guardar la nueva clave de acceso en el sistema
            cliente.setClave(codificador.codificar(nueva));
            clienteRepository.guardar(cliente);
        }
    }
}