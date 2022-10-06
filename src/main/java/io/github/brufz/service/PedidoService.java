package io.github.brufz.service;

import io.github.brufz.model.Pedido;
import io.github.brufz.rest.dto.PedidoDto;

public interface PedidoService {
    //vai receber o pedido dto e salvar na base de dados
    Pedido salvar(PedidoDto dto);
}
