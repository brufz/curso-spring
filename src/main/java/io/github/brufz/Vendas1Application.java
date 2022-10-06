package io.github.brufz;

import io.github.brufz.model.Cliente;
import io.github.brufz.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Vendas1Application {

	public static void main(String[] args) {
		SpringApplication.run(Vendas1Application.class, args);
	}

}
