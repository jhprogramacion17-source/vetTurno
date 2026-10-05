package com.huellitas.vetTurno.service;
import com.huellitas.vetTurno.dto.MascotaDTO;
import com.huellitas.vetTurno.dto.MascotaRequest;
import com.huellitas.vetTurno.model.Mascota;
import com.huellitas.vetTurno.model.Propietario;
import com.huellitas.vetTurno.repository.MascotaRepository;
import com.huellitas.vetTurno.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;

    public MascotaService(MascotaRepository mascotaRepository, PropietarioRepository propietarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
    }

    public MascotaDTO crearMascota(MascotaRequest request) {
        Propietario propietario = propietarioRepository.findById(request.getPropietarioId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el propietario con ID: " + request.getPropietarioId()));

        Mascota mascota = new Mascota(
                request.getNombre(),
                request.getEspecie(),
                request.getRaza(),
                propietario
        );

        Mascota guardada = mascotaRepository.save(mascota);
        return mapearADTO(guardada);
    }

    public List<MascotaDTO> listarMascotas() {
        return mascotaRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private MascotaDTO mapearADTO(Mascota m) {
        return new MascotaDTO(
                m.getId(),
                m.getNombre(),
                m.getEspecie(),
                m.getRaza(),
                m.getPropietario().getId(),
                m.getPropietario().getNombre()
        );
    }
}