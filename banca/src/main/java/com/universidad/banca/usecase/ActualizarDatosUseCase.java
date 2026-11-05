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

        // Criterio C2 (HU-04): Validación de formatos de correo, teléfono y dirección
        if (correo == null || !correo.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido.");
        }
        if (telefono == null || !telefono.trim().matches("^\\d{7,10}$")) {
            throw new IllegalArgumentException("Formato de teléfono inválido: debe contener entre 7 y 10 dígitos numéricos.");
        }
        if (direccion == null || direccion.trim().length() < 5) {
            throw new IllegalArgumentException("Formato de dirección inválido: debe tener al menos 5 caracteres.");
        }

        // Criterio C1 (HU-04): Actualizar los datos personales y notificar éxito
        cliente.setCorreo(correo.trim());
        cliente.setTelefono(telefono.trim());
        cliente.setDireccion(direccion.trim());

        return clienteRepository.guardar(cliente);
    }
}