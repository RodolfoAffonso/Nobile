package com.rodolfoafonso.nobile.dto;

import com.rodolfoafonso.nobile.domain.enums.AccountStatus;
import jakarta.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    @NotBlank(message = "Campo obrigatório")
    private String name;

    @NotBlank(message = "Campo obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "A senha deve conter letra maiúscula, minúscula, número e caractere especial"
    )
    private String password;
    private String profilePicture;
    private String role; // BUYER ou SELLER
    private String status; // pode vir do cliente

}
