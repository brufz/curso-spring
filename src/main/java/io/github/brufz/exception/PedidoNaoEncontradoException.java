package io.github.brufz.exception;

public class PedidoNaoEncontradoException extends RuntimeException {
	
	public PedidoNaoEncontradoException () {
		super("Pedido não encontrado");
	}
}
