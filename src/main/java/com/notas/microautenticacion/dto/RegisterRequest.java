package com.notas.microautenticacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Email
    @NotEmpty(message = " Email is required ")
    private String email;
    @NotBlank(message = " Username is required ")
    private String username;
    @NotBlank (message = " Password is required ")
    private String password;

}
