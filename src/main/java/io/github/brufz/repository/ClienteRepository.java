package io.github.brufz.repository;

import io.github.brufz.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
    List<Cliente> findByNomeLike(String nome);

    //outra forma de fazer a Query findByNome
    //@Query(value = " select c from Cliente c where c.nome like :nome ")
    //List<Cliente> encontrarPorNome(@Param("nome") String nome);
    boolean existsByNome(String nome);
    @Modifying
    void deleteByNome(String nome);

    @Query( " select c from Cliente c left join fetch c.pedidos where c.id = :id ") //traz o cliente tendo pedidos ou nao (left)
    Cliente findClienteFetchPedidos(@Param("id") Integer id);



}
