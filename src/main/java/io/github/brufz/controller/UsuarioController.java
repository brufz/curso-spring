package io.github.brufz.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.brufz.exception.SenhaInvalidaException;
import io.github.brufz.model.Usuario;
import io.github.brufz.rest.dto.CredenciaisDTO;
import io.github.brufz.rest.dto.TokenDTO;
import io.github.brufz.security.jwt.JwtService;
import io.github.brufz.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioServiceImpl usuarioService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtService jwtService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	 @ApiOperation("Cadastrar um novo usuário")
    @ApiResponses({
    	@ApiResponse(code = 200, message = "Usuário cadastrado com sucesso"),
    	@ApiResponse(code = 400, message = "Erro ao cadastrar usuário")
    })
	public Usuario salvar(@RequestBody @Valid Usuario usuario) {
		String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		return usuarioService.salvar(usuario);
	
	}
	
	 @ApiOperation("Autenticar usuário")
	 @ApiResponses({
	    	@ApiResponse(code = 200, message = "Usuário autenticado com sucesso"),
	    	@ApiResponse(code = 400, message = "Erro ao autenticar usuário")
	    })
	@PostMapping("/auth")
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			Usuario usuario = Usuario
					.builder()
						.login(credenciais.getLogin())
						.senha(credenciais.getSenha())
						.build();
						
			UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
			String token = jwtService.gerarToken(usuario);
			
			return new TokenDTO(usuario.getLogin(), token);
		} catch (UsernameNotFoundException | SenhaInvalidaException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} 
	}

}
