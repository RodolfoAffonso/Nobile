package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.UserRole;
import com.rodolfoafonso.nobile.dto.AuthResponseDTO;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.exception.BusinessRuleException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveMockarFindByEmail(){
        Mockito.when(userRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(new User()));

        // Act
        Optional<User> resultado = userRepository.findByEmail("teste@email.com");

        // Assert
        assertTrue(resultado.isPresent());
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        System.out.println("Mock repo no teste: " + userRepository.getClass());

        UserDTO dto = new UserDTO();
        dto.setName("Rodolfo");
        dto.setEmail("rodolfo@email.com");
        dto.setPassword("123456");

        UserRole role = UserRole.USER;
        dto.setRole(role.name());

        User mappedUser = new User();
        mappedUser.setId(1L);
        mappedUser.setName(dto.getName());
        mappedUser.setEmail(dto.getEmail());

        Mockito.when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(dto.getPassword()))
                .thenReturn("encoded123");
        Mockito.when(userMapper.mapper(dto))
                .thenReturn(mappedUser);
        Mockito.when(userRepository.save(mappedUser))
                .thenReturn(mappedUser);
        Mockito.when(tokenService.generateToken(mappedUser))
                .thenReturn("fake-jwt-token");

            AuthResponseDTO response = authService.register(dto, role);

            Assertions.assertEquals("fake-jwt-token", response.getToken());
            Mockito.verify(userRepository).save(mappedUser);
        }


    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setEmail("rodolfo@email.com");

        Mockito.when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new User()));

        // Act + Assert
        Assertions.assertThrows(BusinessRuleException.class,
                () -> authService.register(dto, UserRole.USER));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }
}
