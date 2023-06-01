package com.notas.microautenticacion.controller;

import com.notas.microautenticacion.dto.*;
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
	public ResponseEntity<?> signup (@Valid @RequestBody RegisterRequest registerRequest) {
		SignupResponse signupResponse = new SignupResponse(authService.signup(registerRequest).getUserId());
		return ResponseEntity.ok(signupResponse);
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
	@PostMapping("/validar")
	public ResponseEntity<?> validarToken(@RequestBody ValidationRequest validationRequest) {
		return ResponseEntity.ok(authService.validarToken(validationRequest));
	}

}
