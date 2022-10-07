package io.github.brufz.rest.dto;


public class ItemPedidoDto {
    private Integer produto;
    private Integer quantidade;
    
	public ItemPedidoDto(Integer produto, Integer quantidade) {
		this.produto = produto;
		this.quantidade = quantidade;
	}
	
	public ItemPedidoDto() {
		
	}

	public Integer getProduto() {
		return produto;
	}

	public void setProduto(Integer produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
    
	
    


}
