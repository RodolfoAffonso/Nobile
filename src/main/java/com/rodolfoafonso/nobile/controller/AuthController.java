package com.rodolfoafonso.nobile.controller;

import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserDTO dto) {
        UserRole userRole = UserRole.valueOf(dto.getRole().toUpperCase());
        return ResponseEntity.ok(authService.register(dto, userRole));
    }
}
