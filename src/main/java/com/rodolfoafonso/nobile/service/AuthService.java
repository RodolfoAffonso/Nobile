package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.AuthenticationDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.exception.BusinessRuleException;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserMapper mapper;
    private final TokenService tokenService;
    private final PasswordResetTokenService passwordResetTokenService;


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

    // Solicita redefinição de senha: gera token e envia e-mail
    public void requestPasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com email: " + email));

        String token = passwordResetTokenService.generateResetToken(user.getEmail());
        emailService.sendPasswordResetEmail(user.getEmail(), user.getName(),token);
    }

    // Redefine senha usando token JWT
    public void resetPassword(String token, String newPassword) {
        String email = passwordResetTokenService.validateToken(token);
        if (email.isEmpty()) {
            throw new RuntimeException("Token inválido ou expirado.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenService.invalidateToken(token);
    }


}
