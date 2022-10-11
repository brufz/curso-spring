package io.github.brufz.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.brufz.APIErrors;
import io.github.brufz.exception.PedidoNaoEncontradoException;
import io.github.brufz.exception.RegraNegocioException;

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
    
    // tratando o erro que aparece na validação
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrors handleMethodNotValidException(MethodArgumentNotValidException ex) {
    	List<String> errors = ex.getBindingResult().getAllErrors().stream()
    	.map(erro -> erro.getDefaultMessage())
    	.collect(Collectors.toList());
    	
    	return new APIErrors(errors);
    }

	
}
