package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.dto.UserUpdateDTO;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private UserResponseDTO userResponseDTO;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Rodolfo");
        user.setEmail("rodolfo@email.com");

        userDTO = new UserDTO();
        userDTO.setName("Rodolfo");
        userDTO.setEmail("rodolfo@email.com");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName("Rodolfo");
        userResponseDTO.setEmail("rodolfo@email.com");

        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("Novo Nome");
    }

    @Test
    void deveAtualizarUserExistente() {
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // mocks repository e mapper
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.mapper(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.update(userUpdateDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
    }


    @Test
    void deveLancarNotFoundExceptionNoUpdate() {
        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("email@invalido.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Mock repository retorna vazio
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // Verifica a exceção
        assertThrows(NotFoundException.class,
                () -> userService.update(userUpdateDTO));
    }

    @Test
    void deveRetornarTodosUsers() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(mapper.mapperResponse(user)).thenReturn(userResponseDTO);

        // when
        List<UserResponseDTO> result = userService.search();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponseDTO.getEmail(), result.get(0).getEmail());
    }

    @Test
    void shouldReturnUserWhenEmailExists() {
        // given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapper.mapperResponse(user)).thenReturn(userResponseDTO);

        // when
        UserResponseDTO result = userService.searchByEmail(user.getEmail());

        // then
        assertNotNull(result);
        assertEquals(userResponseDTO.getEmail(), result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        // given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class,
                () -> userService.searchByEmail(user.getEmail()));
    }

}
