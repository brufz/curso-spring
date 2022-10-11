package io.github.brufz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.brufz.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	Optional<Usuario> findByLogin(String login);
}
