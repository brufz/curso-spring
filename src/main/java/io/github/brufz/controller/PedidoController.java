package io.github.brufz.controller;


import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.brufz.enuns.StatusPedido;
import io.github.brufz.model.ItemPedido;
import io.github.brufz.model.Pedido;
import io.github.brufz.rest.dto.AtualizacaoStatusPedidoDto;
import io.github.brufz.rest.dto.InformacaoItemPedidoDto;
import io.github.brufz.rest.dto.InformacoesPedidoDto;
import io.github.brufz.rest.dto.PedidoDto;
import io.github.brufz.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    //injecao de dependencia sem Autowired
    private PedidoService service;

    public PedidoController(PedidoService service){
        this.service = service;
    }
    
    //@Autowired
    //private PedidoService service;

    //vai retornar o id do pedido gerado
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody PedidoDto dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }
    
    @GetMapping("/{id}")
    public InformacoesPedidoDto getById(@PathVariable Integer id) {
    	return service
    			.obterPedidoCompleto(id)
    			.map(p -> converter(p))
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
    
    //converte o pedido em informações do pedido
    private InformacoesPedidoDto converter(Pedido pedido) {
    	return InformacoesPedidoDto
    			.builder()
    			.codigo(pedido.getId())
    			.dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    			.cpf(pedido.getCliente().getCpf())
    			.nomeCliente(pedido.getCliente().getNome())
    			.total(pedido.getTotal())
    			.status(pedido.getStatus().name())
    			.itens(converter(pedido.getItens()))
    			.build();
    }
    
    private List<InformacaoItemPedidoDto> converter(List<ItemPedido> itens){
    	if(org.springframework.util.CollectionUtils.isEmpty(itens)) {
    		return Collections.emptyList();
    	}
    	return itens.stream()
    			.map( item -> InformacaoItemPedidoDto
    					.builder()
    					.descricaoProduto(item.getProduto().getDescricao())
    					.precoUnitario(item.getProduto().getPreco())
    					.quantidade(item.getQuantidade())
    					.build()
    			).collect(Collectors.toList());
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void updateStatus(@RequestBody AtualizacaoStatusPedidoDto dto,
    						@PathVariable Integer id) {
    	String novoStatus = dto.getNovoStatus();
    	service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }
}
