package com.huellitas.vetTurno.service;
import com.huellitas.vetTurno.dto.VeterinarioDTO;
import com.huellitas.vetTurno.dto.VeterinarioRequest;
import com.huellitas.vetTurno.model.Veterinario;
import com.huellitas.vetTurno.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    public VeterinarioDTO crearVeterinario(VeterinarioRequest request) {
        Veterinario veterinario = new Veterinario(
                request.getNombre(),
                request.getEspecialidad()
        );
        Veterinario guardado = veterinarioRepository.save(veterinario);
        return mapearADTO(guardado);
    }

    public List<VeterinarioDTO> listarVeterinarios() {
        return veterinarioRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private VeterinarioDTO mapearADTO(Veterinario v) {
        return new VeterinarioDTO(v.getId(), v.getNombre(), v.getEspecialidad());
    }
}
