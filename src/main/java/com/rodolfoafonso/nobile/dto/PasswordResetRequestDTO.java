package com.rodolfoafonso.nobile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PasswordResetRequestDTO {

    @NotBlank
    @Email
    private String email;
}
