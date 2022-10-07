package io.github.brufz.controller;

import io.github.brufz.APIErrors;
import io.github.brufz.exception.PedidoNaoEncontradoException;
import io.github.brufz.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    //fazer tratamento utilizando ExceptionHandlers, que são métodos que quando algum erro é lançado,
    //eles capturam, e podemos fazer um tratamento, como retornando outro código de status

    //toda vez que a API cair no RegraNegocioException vai acionar o ExceptionHandler
    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrors handleRegraNegocioException(RegraNegocioException ex){
        String mensagemErro = ex.getMessage();
        return new APIErrors(mensagemErro);
    }
    
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIErrors handlePedidoNotFoundException(PedidoNaoEncontradoException ex) {
    	return new APIErrors(ex.getMessage());
    }
}
