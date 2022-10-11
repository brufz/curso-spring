package io.github.brufz.rest.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.github.brufz.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {
	@NotNull(message = "Informe o código do cliente")
    private Integer cliente;
	@NotNull(message = "Campo total do pedido é obrigatório!")
    private BigDecimal total;
	@NotEmptyList(message = "Pedido não pode ser realizado sem itens")
    private List<ItemPedidoDto> itens;
    

	
}
