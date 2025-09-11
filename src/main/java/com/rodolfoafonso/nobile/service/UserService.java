package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserDTO update(String email, UserDTO userDTO) {
        Optional<User> optional = userRepository.findByEmail(email);
        optional.orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        userDTO.setEmail(email);
        User user = mapper.mapper(userDTO);
        User userSave = userRepository.save(user);
        return mapper.mapper(userSave);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
