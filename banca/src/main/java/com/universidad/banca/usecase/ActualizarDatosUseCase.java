package com.universidad.banca.usecase;

import com.universidad.banca.domain.model.Cliente;
import com.universidad.banca.domain.repository.ClienteRepository;

public class ActualizarDatosUseCase {

    private final ClienteRepository clienteRepository;

    public ActualizarDatosUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente ejecutar(Long clienteId, String correo, String telefono, String direccion) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el cliente con ID: " + clienteId));

        boolean cambiaCorreo = correo != null && !correo.isBlank();
        boolean cambiaTelefono = telefono != null && !telefono.isBlank();
        boolean cambiaDireccion = direccion != null && !direccion.isBlank();

        if (!cambiaCorreo && !cambiaTelefono && !cambiaDireccion) {
            throw new IllegalArgumentException("Debe ingresar al menos un dato para actualizar.");
        }

        // Criterio C2 (HU-04): se validan solo los datos que el usuario quiere cambiar
        if (cambiaCorreo && !correo.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido.");
        }
        if (cambiaTelefono && !telefono.trim().matches("^\\d{7,10}$")) {
            throw new IllegalArgumentException("Formato de teléfono inválido: debe contener entre 7 y 10 dígitos numéricos.");
        }
        if (cambiaDireccion && direccion.trim().length() < 5) {
            throw new IllegalArgumentException("Formato de dirección inválido: debe tener al menos 5 caracteres.");
        }

        // Criterio C1 (HU-04): se actualizan solo los datos enviados
        if (cambiaCorreo) {
            cliente.setCorreo(correo.trim());
        }
        if (cambiaTelefono) {
            cliente.setTelefono(telefono.trim());
        }
        if (cambiaDireccion) {
            cliente.setDireccion(direccion.trim());
        }

        return clienteRepository.guardar(cliente);
    }
}