package br.com.cotiinformatica.domain.dtos;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CriarClienteRequestDto {

	@Pattern(regexp = "^[a-zA-ZÀ-ÿ'.\s]{8,100}$", message = "Por favor, insira um nome válido.")
	@NotEmpty(message = "Por favor, informe o nome do cliente.")
	private String nome;

	@Email(message = "Por favor, informe um email válido.")
	@NotEmpty(message = "Por favor, informe o email do cliente.")
	private String email;
	
	@Pattern(regexp = "^\\d{11}$", message = "Por favor, insira um CPF contendo apenas 11 dígitos.")
	@NotEmpty(message = "Por favor, informe o CPF do cliente.")
	private String cpf;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past(message = "Por favor, informe uma data no passado.")
	@NotNull(message = "Por favor, informe a data de nascimento do cliente.")
	private Date dataNascimento;

	@NotNull(message = "Por favor, informe as informações do endereço do cliente.")
	private CriarEnderecoRequestDto endereco;
}
