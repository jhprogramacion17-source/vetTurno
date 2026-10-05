package com.huellitas.vetTurno.service;
import com.huellitas.vetTurno.dto.CitaDTO;
import com.huellitas.vetTurno.dto.CitaRequest;
import com.huellitas.vetTurno.model.Cita;
import com.huellitas.vetTurno.model.Mascota;
import com.huellitas.vetTurno.model.Veterinario;
import com.huellitas.vetTurno.repository.CitaRepository;
import com.huellitas.vetTurno.repository.MascotaRepository;
import com.huellitas.vetTurno.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;

    public CitaService(CitaRepository citaRepository,
                       MascotaRepository mascotaRepository,
                       VeterinarioRepository veterinarioRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public CitaDTO crear(CitaRequest request) {

        Mascota mascota = mascotaRepository.findById(request.getMascotaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una mascota con id " + request.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findById(request.getVeterinarioId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un veterinario con id " + request.getVeterinarioId()));


        if (request.getFechaHora() == null || !request.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora de la cita debe ser futura");
        }


        if (citaRepository.existsByVeterinarioIdAndFechaHora(
                veterinario.getId(), request.getFechaHora())) {
            throw new IllegalArgumentException(
                    "El veterinario ya tiene una cita en ese horario");
        }


        Cita cita = new Cita();
        cita.setFechaHora(request.getFechaHora());
        cita.setMotivo(request.getMotivo());
        cita.setMascota(mascota);
        cita.setVeterinario(veterinario);

        return aDTO(citaRepository.save(cita));
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listar() {
        return citaRepository.findAll().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorVeterinario(Long veterinarioId) {
        if (!veterinarioRepository.existsById(veterinarioId)) {
            throw new IllegalArgumentException(
                    "No existe un veterinario con id " + veterinarioId);
        }
        return citaRepository.findByVeterinarioIdOrderByFechaHoraAsc(veterinarioId)
                .stream().map(this::aDTO).toList();
    }

    private CitaDTO aDTO(Cita c) {
        return new CitaDTO(
                c.getId(),
                c.getFechaHora(),
                c.getMotivo(),
                c.getMascota().getNombre(),
                c.getMascota().getPropietario().getNombre(),
                c.getVeterinario().getNombre());
    }
}