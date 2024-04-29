package br.com.cotiinformatica.domain.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "tbl_cliente")
@Data
public class Cliente {

	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "nome", length = 150, nullable = false)
	private String nome;

	@Column(name = "email", length = 100, nullable = false)
	private String email;

	@Column(name = "cpf", length = 11, nullable = false, unique = true)
	private String cpf;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@Column(name = "foto", nullable = false)
	private byte[] foto;
	
	@OneToMany(mappedBy = "cliente")
	private List<Endereco> enderecos;
}
