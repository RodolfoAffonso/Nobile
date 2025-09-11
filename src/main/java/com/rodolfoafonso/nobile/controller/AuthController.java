package com.rodolfoafonso.nobile.controller;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.AuthenticationDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.service.AuthService;
import com.rodolfoafonso.nobile.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.remote.JMXAuthenticator;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        AuthResponseDTO response = authService.login(data);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserDTO dto) {
        UserRole userRole = UserRole.valueOf(dto.getRole().toUpperCase());
        return ResponseEntity.ok(authService.register(dto, userRole)) ;
    }
}
