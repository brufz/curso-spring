package io.github.brufz.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import io.github.brufz.Vendas1Application;
import io.github.brufz.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	
	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;
	
	public String gerarToken(Usuario usuario) {
		long expString = Long.valueOf(expiracao); //alterando String para long
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString); //pegando a data e hora atual e somando os minutos da expiracao
		Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		Date data = Date.from(instant);
		
		return Jwts
				.builder()
				.setSubject(usuario.getLogin())
				.setExpiration(data)
				.signWith(SignatureAlgorithm.HS512, chaveAssinatura)
				.compact();
	}
	
	private Claims obterClaims(String token) throws ExpiredJwtException{ //se o token estiver expirado vai gerar um erro
		return Jwts
				.parser() //decodifica o token
				.setSigningKey(chaveAssinatura) //qual chave usei para gerar o token
				.parseClaimsJws(token)
				.getBody();
	}
	
	public boolean tokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			Date dataExpiracao = claims.getExpiration();
			LocalDateTime data = 
					dataExpiracao.toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDateTime(); //convertendo objeto date em LocalDateTime
			return !LocalDateTime.now().isAfter(data); //token é valido quando a data e hora atual NAO é depois da hora de expiracao do token
		} catch (Exception e) {
			return false;
		}
	}
	
	public String obterLoginUsuario(String token) throws ExpiredJwtException{
		return (String) obterClaims(token).getSubject();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Vendas1Application.class);
		JwtService service = context.getBean(JwtService.class);
		Usuario usuario = Usuario.builder().login("Fulano").build();
		String token = service.gerarToken(usuario);
		System.out.println(token);
		
		boolean isTokenValido = service.tokenValido(token);
		System.out.println("O token está valido? " + isTokenValido);
		
		System.out.println(service.obterLoginUsuario(token));
	}
}
