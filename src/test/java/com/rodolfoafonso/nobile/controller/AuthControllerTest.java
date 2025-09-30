package com.rodolfoafonso.nobile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.AuthenticationDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.repository.UserRepository;
import com.rodolfoafonso.nobile.service.AuthService;
import com.rodolfoafonso.nobile.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // desativa filtros de segurança para facilitar testes
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("rodolfo");
        userDTO.setEmail("teste@email.com");
        userDTO.setPassword("1fdmsf2eMn@");
        userDTO.setRole("USER");

        AuthResponseDTO responseDTO = new AuthResponseDTO("token-fake");

        when(authService.register(any(UserDTO.class), eq(UserRole.USER)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token-fake"));
    }

    @Test
    void login_DeveRetornarToken() throws Exception {
        AuthenticationDTO loginRequest = new AuthenticationDTO("user@email.com", "123456");
        AuthResponseDTO mockResponse = new AuthResponseDTO("mocked-jwt-token");

        Mockito.when(authService.login(any(AuthenticationDTO.class)))
                .thenReturn(mockResponse);

        // Act + Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }
    @BeforeEach
    void setup() {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Test
    void login_DeveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        AuthenticationDTO authDTO = new AuthenticationDTO("email@teste.com", "senhaErrada");

        // Simula que o AuthService lança BadCredentialsException
        when(authService.login(any(AuthenticationDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized());
    }


}
