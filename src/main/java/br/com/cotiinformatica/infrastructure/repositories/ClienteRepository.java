package br.com.cotiinformatica.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cotiinformatica.domain.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

	@Query("select cl from Cliente cl left join fetch cl.enderecos order by cl.nome")
	List<Cliente> findAllByNome();
	
	@Query("select cl from Cliente cl left join fetch cl.enderecos where cl.id = :pId")
	Optional<Cliente> findById(@Param("pId") UUID id);
	
	@Query("select cl from Cliente cl left join fetch cl.enderecos where cl.cpf = :pCpf")
	Optional<Cliente> findByCpf(@Param("pCpf") String cpf);
}
