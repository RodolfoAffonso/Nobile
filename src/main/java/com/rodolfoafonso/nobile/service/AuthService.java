package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.AuthenticationDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.exception.BusinessRuleException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class AuthService  {

    private final  AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final TokenService tokenService;


    public AuthResponseDTO login(@Valid AuthenticationDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO register(UserDTO dto, UserRole userRole) {

        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new BusinessRuleException("E-mail já cadastrado: " + dto.getEmail());
        });

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encryptedPassword);
        dto.setRole(userRole.name());

        User user = mapper.mapper(dto);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token);
    }


}
