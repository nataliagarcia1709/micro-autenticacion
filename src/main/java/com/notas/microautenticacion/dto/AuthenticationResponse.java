package com.notas.microautenticacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String authenticationToken;
}