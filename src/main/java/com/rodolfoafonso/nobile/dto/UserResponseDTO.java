package com.rodolfoafonso.nobile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String name;
    private String email;
    private String profilePicture;
    private String role; // BUYER ou SELLER
    private String status; // pode vir do cliente

}
