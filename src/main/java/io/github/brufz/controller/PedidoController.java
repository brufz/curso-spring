package io.github.brufz.controller;

import io.github.brufz.model.Pedido;
import io.github.brufz.rest.dto.PedidoDto;
import io.github.brufz.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    //injecao de dependencia sem Autowired
    private PedidoService service;

    public PedidoController(PedidoService service){
        this.service = service;
    }

    //vai retornar o id do pedido gerado
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody PedidoDto dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }


}
