package io.github.brufz.controller;

import io.github.brufz.model.Cliente;
import io.github.brufz.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/{id}") //se eu usar um nome diferente do parametro tenhoque usar @Param
    public Cliente getClienteById(@PathVariable Integer id) throws ResponseStatusException {
        return clienteRepository
                .findById(id) //sucesso
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")); //erro
    }

    @GetMapping
    public List<Cliente> find (Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

       return clienteRepository.findAll(example);

    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id ,
                       @RequestBody Cliente cliente) throws ResponseStatusException{
        clienteRepository.findById(id)
                                .map( clienteExistente -> {
                                   cliente.setId(clienteExistente.getId());
                                   clienteRepository.save(cliente);
                                   return clienteExistente;
                                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                            "Cliente não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //define o código de status que retorna quando não é 200 - OK
    public Cliente save(@RequestBody Cliente cliente){
        return clienteRepository.save(cliente);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws ResponseStatusException {
        clienteRepository.findById(id)
                .map(cliente -> { //método map tem que ter um return
                    clienteRepository.delete(cliente);
                    return Void.class;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
