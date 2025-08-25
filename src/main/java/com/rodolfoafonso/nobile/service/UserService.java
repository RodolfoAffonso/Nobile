package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.exception.NotExistUserException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserDTO update(String email, UserDTO userDTO) {
        Optional<User> optional = userRepository.findByEmail(email);
        optional.orElseThrow(NotExistUserException::new);
        userDTO.setEmail(email);
        User user = mapper.mapper(userDTO);
        User userSave = userRepository.save(user);
        return mapper.mapper(userSave);
    }


    public List<UserDTO> search() {
        return userRepository.findAll()
                .stream()
                .map(mapper::mapper) // converte User -> UserDTO
                .collect(Collectors.toList());
    }


    public UserDTO searchByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        optional.orElseThrow(NotExistUserException::new);
        return mapper.mapper(optional.get());
    }


    public UserDTO deleteByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        optional.orElseThrow(NotExistUserException::new);
        userRepository.deleteById(optional.get().getId());
        return mapper.mapper(optional.get());

    }
}
