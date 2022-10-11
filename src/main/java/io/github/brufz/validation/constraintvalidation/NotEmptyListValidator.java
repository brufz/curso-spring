package io.github.brufz.validation.constraintvalidation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.github.brufz.validation.NotEmptyList;

//essa interface obriga implementar esses dois métodos
//classe que faz a validação da anotation criada
public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List>{

	@Override
	public void initialize(NotEmptyList constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(List list, 
							ConstraintValidatorContext constraintValidatorContext) {
	
		return list != null && !list.isEmpty() ;
	}
	
	

}
