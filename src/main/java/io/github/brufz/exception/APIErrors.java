package io.github.brufz.exception;

import java.util.Arrays;
import java.util.List;

public class APIErrors {

    private List<String> errors;
    
    public APIErrors(List<String> errors) {
		this.errors = errors;
	}

	public APIErrors(String mensagemErro) {
        this.errors = Arrays.asList(mensagemErro);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
