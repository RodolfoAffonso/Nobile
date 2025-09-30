package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.entity.Watch;
import com.rodolfoafonso.nobile.dto.WatchDTO;
import com.rodolfoafonso.nobile.dto.WatchResponseDTO;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.WatchMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import com.rodolfoafonso.nobile.repository.WatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WatchService {

    private final UserRepository  userRepository;
    private final WatchRepository watchRepository;
    private final WatchMapper watchMapper;

    public WatchResponseDTO create(WatchDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User sellerUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com email: " + email));
        // Força associação do seller logado
        Watch watch = watchMapper.toEntity(dto);
        watch.setSeller(sellerUser);

        Watch saved = watchRepository.save(watch);
        return watchMapper.toDto(saved);

    }

    public List<WatchResponseDTO> getAll() {
        return List.of();
    }

    public WatchResponseDTO getById(Long id) {
        return null;
    }

    public WatchResponseDTO update(Long id, WatchDTO dto) {
        return null;
    }

    public void delete(Long id) {
    }
}
