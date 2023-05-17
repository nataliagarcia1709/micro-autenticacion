package com.notas.microautenticacion.controller;

import com.notas.microautenticacion.dto.AuthenticationResponse;
import com.notas.microautenticacion.dto.LoginRequest;
import com.notas.microautenticacion.dto.RegisterRequest;
import com.notas.microautenticacion.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Validated
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup (@Valid @RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return ResponseEntity.ok("Usuario creado exitosamente");
	}

	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token){
		authService.verifyAccount(token);
		return new ResponseEntity<>("Successful user verification", HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
	    return ResponseEntity.ok(authService.login(loginRequest));
	}
}
