package io.github.brufz.rest.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PedidoDto {
    private Integer cliente;
    private BigDecimal total;
    private List<ItemPedidoDto> itens;
    
	public Integer getCliente() {
		return cliente;
	}
	public void setCliente(Integer cliente) {
		this.cliente = cliente;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public List<ItemPedidoDto> getItens() {
		return itens;
	}
	public void setItens(List<ItemPedidoDto> itens) {
		this.itens = itens;
	}
	
	public PedidoDto(Integer cliente, BigDecimal total, List<ItemPedidoDto> itens) {
		super();
		this.cliente = cliente;
		this.total = total;
		this.itens = itens;
	}
	public PedidoDto() {
		super();
	}
	
	
    
    

}
