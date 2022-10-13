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

import io.github.brufz.model.Produto;
import io.github.brufz.repository.ProdutoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um produto")
    @ApiResponses({
    	@ApiResponse(code = 200, message = "Produto encontrado"),
    	@ApiResponse(code = 404, message = "Produto não encontrado para o id informado")
    })
    public Produto getProdutoById(@PathVariable Integer id) throws ResponseStatusException {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping
    @ApiOperation("Obter lista de produtos")
    @ApiResponse(code = 200, message = "Lista de produtos encontrada")
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        return produtoRepository.findAll(example);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar um novo produto")
    @ApiResponses({
    	@ApiResponse(code = 201, message = "Produto cadastrado com sucesso"),
    	@ApiResponse(code = 400, message = "Erro de validação")
    })
    public Produto save(@RequestBody @Valid Produto produto){
        return produtoRepository.save(produto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualizar um  produto")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Produto atualizado com sucesso"),
    	@ApiResponse(code = 404, message = "Produto não encontrado para o id informado")
    })
    public void update(@PathVariable Integer id,
    					@Valid @RequestBody Produto produto){

        produtoRepository.findById(id).map(produtoExistente -> {
            produto.setId(produtoExistente.getId());
            produtoRepository.save(produto);
            return produtoExistente;
        }).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar um  produto")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Produto deletado com sucesso"),
    	@ApiResponse(code = 404, message = "Produto não encontrado para o id informado")
    })
    public void deleteById(@PathVariable Integer id){
        produtoRepository.findById(id)
                .map(produto -> {
                    produtoRepository.delete(produto);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
}


