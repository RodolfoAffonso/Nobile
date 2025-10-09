package com.rodolfoafonso.nobile.controller;


import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.*;
import com.rodolfoafonso.nobile.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        AuthResponseDTO response = authService.login(data);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserDTO dto) {
        UserRole userRole = UserRole.valueOf(dto.getRole().toUpperCase());
        AuthResponseDTO response = authService.register(dto, userRole);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid PasswordResetRequestDTO dto) throws MessagingException {
        authService.requestPasswordReset(dto.getEmail());
        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso!");
    }


    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetDTO dto) {
        authService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

}
