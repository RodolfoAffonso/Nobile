package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Test
    void deveAtualizarUserExistente() {
        // given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapper.mapper(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.mapper(any(User.class))).thenReturn(userDTO);

        // when
        UserDTO result = userService.update(user.getEmail(), userDTO);

        // then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarNotFoundExceptionNoUpdate() {
        // given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class,
                () -> userService.update(user.getEmail(), userDTO));
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
