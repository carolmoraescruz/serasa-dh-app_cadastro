package br.com.app_cadastro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app_cadastro.repository.UserRepository;
import br.com.app_cadastro.security.CredenciaisContaVO;
import br.com.app_cadastro.security.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping(value="/signin",
			produces={"application/json", "application.xml"},
			consumes={"application.json", "application.xml"})
	public ResponseEntity signin(@RequestBody CredenciaisContaVO cred) {
		try {
			var username = cred.getUserName();
			var password = cred.getPassword();
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			var user = repository.findByUserName(username);
			var token = "";
			
			if (user != null) {
				token = jwtProvider.createToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Usuário " + username + " não localizado");
			}
			
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
			
			return ResponseEntity.ok(model);
		}
		catch (AuthenticationException e) {
			throw new BadCredentialsException("Usuário ou senha inválidos.");
		}
		
	}

}
