package com.rodolfoafonso.nobile.controller;

import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.dto.UserResponseDTO;
import com.rodolfoafonso.nobile.dto.UserUpdateDTO;
import com.rodolfoafonso.nobile.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService  userService;

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<UserDTO> updateUser( @RequestBody @Valid UserUpdateDTO userUpdateDTO ){
        UserDTO userUpdate = userService.update(userUpdateDTO);
        return ResponseEntity.ok(userUpdate);
    }

    @GetMapping
    public List<UserResponseDTO> searchUser(){
        return userService.search();

    }
    @GetMapping("/{email}")
    public UserResponseDTO searchUserbyEmail(@PathVariable String email){
        return userService.searchByEmail(email);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getLoggedUser() {
        UserResponseDTO userResponse = userService.getLoggedUser();
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/me/deactivate")
    public ResponseEntity<Void> deactivateAccount(@AuthenticationPrincipal Authentication authentication) {
        userService.deactivateAccount(authentication);
        return ResponseEntity.noContent().build();
    }


}
