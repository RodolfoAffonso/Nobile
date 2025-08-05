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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class AuthService implements UserDetailsService {


    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final TokenService tokenService;

    public AuthResponseDTO register(UserDTO dto, UserRole userRole) {

        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ExistingEmailException("E-mail já cadastrado: " + dto.getEmail());
        });

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        dto.setRole(userRole.name());

        User user = mapper.mapper(dto);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

}
