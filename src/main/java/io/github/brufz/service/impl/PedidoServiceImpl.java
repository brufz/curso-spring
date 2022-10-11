package io.github.brufz.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import io.github.brufz.enuns.StatusPedido;
import io.github.brufz.exception.PedidoNaoEncontradoException;
import io.github.brufz.exception.RegraNegocioException;
import io.github.brufz.model.Cliente;
import io.github.brufz.model.ItemPedido;
import io.github.brufz.model.Pedido;
import io.github.brufz.model.Produto;
import io.github.brufz.repository.ClienteRepository;
import io.github.brufz.repository.ItensPedidoRepository;
import io.github.brufz.repository.PedidoRepository;
import io.github.brufz.repository.ProdutoRepository;
import io.github.brufz.rest.dto.ItemPedidoDto;
import io.github.brufz.rest.dto.PedidoDto;
import io.github.brufz.service.PedidoService;

//regras de negócio sao implementadas na camada de serviço
@Service
public class PedidoServiceImpl implements PedidoService{

    //injetando as dependencias
    private PedidoRepository repository;
    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private ItensPedidoRepository itensPedidoRepository;

    public PedidoServiceImpl(PedidoRepository repository,
                             ClienteRepository clientes,
                             ProdutoRepository produtos,
                             ItensPedidoRepository itens)
    {
        this.repository = repository;
        this.clienteRepository = clientes;
        this.produtoRepository = produtos;
        this.itensPedidoRepository = itens;
    }

    //sobrescrevendo método que esta no PedidoService
    //método responsável por salvar o pedido que veio do dto
    @Transactional //se acontecer qualquer erro vai dar um rollback -> ou faz tudo ou não faz nada
    @Override
    public Pedido salvar(PedidoDto dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepository
                .findById(idCliente) //se existir o cliente vai popular o objeto
                .orElseThrow(() -> new RegraNegocioException("código de cliente inválido")); //caso nao ache o id vai ser lançado um erro

        Pedido pedido = new Pedido(); //esta criando um novo pedido, quando salvar será gerado o id do pedido
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now()); //gera data de agora
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedidos = converterItens(pedido, dto.getItens());
        repository.save(pedido); //a partir desse momento a entidade pedido esta salva no banco e obtem o id
        itensPedidoRepository.saveAll(itensPedidos); //salvando os itens na base de dados, agora o pedido está completo
        pedido.setItens(itensPedidos);
        return pedido;
    }
    //método auxiliar responsável por converter os itens em itens pedidos
    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDto> itens){
        if(itens.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens");
        }
        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtoRepository.findById(idProduto)
                                        .orElseThrow(() ->
                                                new RegraNegocioException("Codigo de produto inválido" + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);

                    return itemPedido;
                }).collect(Collectors.toList()); //converte o itempedido em uma lista (tem que usar por causa do .stream .map)

    }

	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		return repository.findByIdFetchItens(id);
	}

	@Override
	@Transactional
	public void atualizaStatus(Integer id, StatusPedido statusPedido) {
		repository.findById(id).map(pedido -> {
			pedido.setStatus(statusPedido);
			return repository.save(pedido);
		}).orElseThrow(() -> new PedidoNaoEncontradoException());
		
	}
}
