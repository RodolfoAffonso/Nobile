package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.exception.ExistingEmailException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class AuthService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper mapper;

    public AuthResponseDTO register(UserDTO dto, UserRole userRole) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ExistingEmailException();
        }

        dto.setRole(userRole.name());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = mapper.mapper(dto);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

}
