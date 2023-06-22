package com.notas.microautenticacion.service;

import com.notas.microautenticacion.dto.*;
import com.notas.microautenticacion.exceptions.PersonalizedException;
import com.notas.microautenticacion.model.NotificationEmail;
import com.notas.microautenticacion.model.User;
import com.notas.microautenticacion.model.VerificationToken;
import com.notas.microautenticacion.repository.UserRepository;
import com.notas.microautenticacion.repository.VerificationTokenRepository;
import com.notas.microautenticacion.config.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;

	private final UserDetailsService userDetailsService;
	private final JwtProvider jwtProvider;

	@Transactional
	public User signup (RegisterRequest registerRequest) {

		if (userRepository.existsByUsername(registerRequest.getUsername())) {
			throw new IllegalArgumentException("El nombre de usuario ya está en uso");
		}

		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new IllegalArgumentException("El correo electrónico ya está en uso");
		}

		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);

		userRepository.save(user);

		String token = generateVerificationToken(user);

		mailService.sendMail(new NotificationEmail("Please Activate your Account",user.getEmail(),"Gracias por registrarte a, " +
				"please click on the below url to activate your account : " +
				"http://localhost:8080/api/auth/accountVerification/" + token));

		return user;
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);

		verificationTokenRepository.save(verificationToken);
		return token;
	}

    public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new PersonalizedException("Invalid Token"));
		enableUser(verificationToken.get());
    }
	private void enableUser(VerificationToken verificationToken) {
		String email= verificationToken.getUser().getEmail();
		User user = userRepository.findByEmail(email).orElseThrow(()->new PersonalizedException("User not found with email "+ email));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest){
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail()); //solo para que me genere la excepcion de UserNotFound y poder manejarlo
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String authenticationToken = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(authenticationToken);
	}

	public ValidationResponse validarToken(ValidationRequest validationRequest){
		User user = userRepository.findByEmail(jwtProvider.validarToken(validationRequest)).get();//buscar usuario por correo obtenido del token
		ValidationResponse validationResponse = new ValidationResponse(user.getUserId(), user.getUsername(), user.getEmail());
		return validationResponse;
	}

}
