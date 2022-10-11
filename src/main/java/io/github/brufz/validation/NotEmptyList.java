package io.github.brufz.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import io.github.brufz.validation.constraintvalidation.NotEmptyListValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)//diz onde que posso colocar a anotation -> exemplo no campo
@Constraint(validatedBy = NotEmptyListValidator.class) //diz que é uma anotation de validação, e passa qual a classe que vai fazer a validação
public @interface NotEmptyList {
	
	//a anotation por default precisa ter esses três métodos:
	
	String message() default "A lista não pode ser vazia";
	Class<?>[] groups() default {};
	Class <? extends Payload>[] payload() default{};

}
