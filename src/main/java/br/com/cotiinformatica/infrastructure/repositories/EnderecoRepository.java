package br.com.cotiinformatica.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cotiinformatica.domain.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

	@Query("select en from Endereco en where en.id = :pId")
	Optional<Endereco> findById(@Param("pId") UUID id);
}
