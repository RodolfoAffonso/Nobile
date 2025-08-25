package com.rodolfoafonso.nobile.controller;

import com.rodolfoafonso.nobile.dto.UserDTO;
import com.rodolfoafonso.nobile.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService  userService;

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UserDTO> updateUser(@PathVariable String email , @RequestBody @Valid UserDTO userDTO ){
        UserDTO tutorResponse = userService.update(email, userDTO);
        return ResponseEntity.ok(tutorResponse);
    }

    @GetMapping
    public List<UserDTO> searchUser(){
        return userService.search();

    }
    @GetMapping("/{email}")
    public UserDTO searchUserbyEmail(@PathVariable String email){
        return userService.searchByEmail(email);
    }

    @DeleteMapping("/{email}")
    public UserDTO deleteUser(@PathVariable String email){
        return userService.deleteByEmail(email);
    }


}
