package com.huellitas.vetTurno.service;
import com.huellitas.vetTurno.dto.PropietarioDTO;
import com.huellitas.vetTurno.dto.PropietarioRequest;
import com.huellitas.vetTurno.model.Propietario;
import com.huellitas.vetTurno.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    public PropietarioDTO crearPropietario(PropietarioRequest request) {
        Propietario propietario = new Propietario(
                request.getNombre(),
                request.getTelefono(),
                request.getEmail()
        );
        Propietario guardado = propietarioRepository.save(propietario);
        return mapearADTO(guardado);
    }

    public List<PropietarioDTO> listarPropietarios() {
        return propietarioRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private PropietarioDTO mapearADTO(Propietario p) {
        return new PropietarioDTO(p.getId(), p.getNombre(), p.getTelefono(), p.getEmail());
    }
}