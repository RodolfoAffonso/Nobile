package com.rodolfoafonso.nobile.service;

import com.rodolfoafonso.nobile.domain.entity.User;
import com.rodolfoafonso.nobile.domain.enums.AccountStatus;
import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.dto.UserUpdateDTO;
import com.rodolfoafonso.nobile.exception.BusinessRuleException;
import com.rodolfoafonso.nobile.exception.NotFoundException;
import com.rodolfoafonso.nobile.mapper.UserMapper;
import com.rodolfoafonso.nobile.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    public UserDTO update(UserUpdateDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com email: " + email));

        Optional.ofNullable(dto.getPassword())
                .ifPresent(newPassword -> validateAndChangePassword(dto, user, newPassword));

        mapper.updateUserFromDto(dto, user);

        return mapper.mapper(userRepository.save(user));
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

    @Transactional
    public void deactivateAccount(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
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

    private void validateAndChangePassword(UserUpdateDTO dto, User user, String newPassword) {
        Optional.ofNullable(dto.getCurrentPassword())
                .filter(current -> !current.isBlank())
                .filter(current -> passwordEncoder.matches(current, user.getPassword()))
                .map(current -> passwordEncoder.encode(newPassword))
                .ifPresentOrElse(
                        user::setPassword,
                        () -> {
                            throw new BusinessRuleException("Senha atual ausente ou incorreta.");
                        }
                );
    }


}