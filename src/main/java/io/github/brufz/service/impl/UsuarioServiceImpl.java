package io.github.brufz.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.brufz.model.Usuario;
import io.github.brufz.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}
	
	// o AuthenticationManagerBuilder vai pegar o usuario do UsuarioService
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados"));
		
		String[] roles = usuario.isAdmin() ? new String [] {"ADMIN", "USER"} : new String[] {"USER"}; //se true -> recebe admin e user, se false -> recebe user
				
		return User
				.builder()
				.username(usuario.getLogin())
				.password(usuario.getSenha())
				.roles(roles)
				.build();
	}

}