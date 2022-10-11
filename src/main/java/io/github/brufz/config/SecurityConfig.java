package io.github.brufz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.brufz.service.impl.UsuarioServiceImpl;

@EnableWebSecurity
@Deprecated
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	//vai carregar os usuarios
	@Lazy
	@Autowired
	private UsuarioServiceImpl usuarioService;

	//precisa de um password encoder, que é uma classe que vai criptografar e descriptografar a senha do usuário
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// vai trazer os objetos que vão fazer autenticação dos usuários e adicionar esses usuários no contexto do security
		auth.
			userDetailsService(usuarioService)
			.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// parte de autorização, vai pegar o usuário que vai se autenticar no método acima e verificar se ele tem autorização pra essa pagina
		http
			.csrf().disable() //configuração que permite que haja uma segurança entre aplicação web e cliente -> modo stateless
			.authorizeRequests()
				.antMatchers("/api/clientes/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/api/produtos/**").hasRole("ADMIN")
				.antMatchers("/api/pedidos/**").hasAnyRole("USER", "ADMIN")
			.and()
				.httpBasic();
		
	}
	
	

}
