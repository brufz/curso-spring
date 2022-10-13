package io.github.brufz.exception;

public class SenhaInvalidaException extends RuntimeException {
	public SenhaInvalidaException () {
		super("Senha inválida");
	}

}
