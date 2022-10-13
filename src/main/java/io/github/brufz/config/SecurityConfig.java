package io.github.brufz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.brufz.security.jwt.JwtAuthFilter;
import io.github.brufz.security.jwt.JwtService;
import io.github.brufz.service.impl.UsuarioServiceImpl;

@EnableWebSecurity
@Deprecated
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Lazy
	@Autowired
	private UsuarioServiceImpl usuarioService;
	@Autowired
	private JwtService jwtService;

	//precisa de um password encoder, que é uma classe que vai criptografar e descriptografar a senha do usuário
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public OncePerRequestFilter jwtFilter() {
		return new JwtAuthFilter(jwtService, usuarioService);
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
				.antMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll() //metodo post permite todos
				.anyRequest().authenticated()
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); 
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**");
	}
	
	
	
	

}
