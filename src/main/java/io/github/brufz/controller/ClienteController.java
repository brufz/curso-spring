package io.github.brufz.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.brufz.model.Cliente;
import io.github.brufz.repository.ClienteRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/{id}") //se eu usar um nome diferente do parametro tenhoque usar @Param
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
    	@ApiResponse(code = 200, message = "Cliente encontrado"),
    	@ApiResponse(code = 404, message = "Cliente não encontrado para o id informado")
    })
    public Cliente getClienteById(@PathVariable Integer id) throws ResponseStatusException {
        return clienteRepository
                .findById(id) //sucesso
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")); //erro
    }

    @GetMapping
    @ApiOperation("Obter lista de clientes")
    @ApiResponse(code = 200, message = "Cliente encontrado")
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
    @ApiOperation("Atualizar um  cliente")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Cliente atualizado com sucesso"),
    	@ApiResponse(code = 404, message = "Cliente não encontrado para o id informado")
    })
    public void update (@PathVariable Integer id ,
                       @Valid @RequestBody Cliente cliente) throws ResponseStatusException{
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
    @ApiOperation("Salvar um novo cliente")
    @ApiResponses({
    	@ApiResponse(code = 201, message = "Cliente cadastrado com sucesso"),
    	@ApiResponse(code = 400, message = "Erro de validação")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente){
        return clienteRepository.save(cliente);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar um  cliente")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Cliente deletado com sucesso"),
    	@ApiResponse(code = 404, message = "Cliente não encontrado para o id informado")
    })
    public void delete(@PathVariable Integer id) throws ResponseStatusException {
        clienteRepository.findById(id)
                .map(cliente -> { //método map tem que ter um return
                    clienteRepository.delete(cliente);
                    return Void.class;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
