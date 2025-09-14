package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.dto.UserUpdateDTO;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO update( UserUpdateDTO userUpdateDTO) {
//        User existingUser = userRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com e-mail: " + email));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com email: " + email));

        // Atualiza apenas os campos não nulos do DTO
        mapper.updateUserFromDto(userUpdateDTO, existingUser);

        // Se enviou nova senha → criptografa
        if (userUpdateDTO.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);
        return mapper.mapper(savedUser);
    }

    public List<UserResponseDTO> search() {
        return userRepository.findAll()
                .stream()
                .map(mapper::mapperResponse) // converte User -> UserDTO
                .collect(Collectors.toList());
    }


    public UserResponseDTO searchByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        optional.orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        return mapper.mapperResponse(optional.get());
    }


    public UserDTO deleteByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        optional.orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        userRepository.deleteById(optional.get().getId());
        return mapper.mapper(optional.get());

    }

    public UserResponseDTO getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com email: " + email));

        return mapper.mapperResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
