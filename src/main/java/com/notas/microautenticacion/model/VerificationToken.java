package com.notas.microautenticacion.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class VerificationToken {//cada vez que un usuario se registra genera un token q se almacena en la bd y envia ese token como parte del enlace de activacion al usuario que corresponda y cuando de clic se busca al usuario asociado con ese token y lo habilita para poder luego iniciar sesion.
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY )
	private Long id ;
	private String token ;
	@OneToOne ( fetch = FetchType.LAZY )
	private User user ;//un token asociado a un usuario.
	private Instant expiryDate ;
}
