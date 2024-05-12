package br.com.cotiinformatica.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AtualizarEnderecoRequestDto {

	@NotNull(message = "Por favor, informe o ID do endereço.")
	private UUID id;
	
	@Size(min = 10, max = 100, message = "Por favor, informe um logradouro válido.")
	@NotEmpty(message = "Por favor, informe o logradouro do cliente.")
	private String logradouro;

	@Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\\s.,]{5,25}$", message = "Por favor, informe um complemento válido.")
	@NotEmpty(message = "Por favor, informe o complemento do endereço.")
	private String complemento;

	@Pattern(regexp = "^\\d{1,5}$", message = "Por favor, informe um número válido.")
	@NotEmpty(message = "Por favor, informe o número do endereço.")
	private String numero;

	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\s]{3,25}$", message = "Por favor, informe um bairro válido.")
	@NotEmpty(message = "Por favor, informe o bairro do cliente.")
	private String bairro;

	@Pattern(regexp = "^[a-zA-ZÀ-ÿ\s]{3,25}$", message = "Por favor, informe uma cidade válida.")
	@NotEmpty(message = "Por favor, informe a cidade do cliente.")
	private String cidade;

	@Pattern(regexp = "^[A-Z]{2}$", message = "Por favor, informe uma sigla com os caracteres maíusculos.")
	@NotEmpty(message = "Por favor, informe a sigla do estado.")
	private String uf;

	@Pattern(regexp = "^\\d{5}-\\d{3}", message = "Por favor, informe um CEP no formato '12345-678'.")
	@NotEmpty(message = "Por favor, informe o CEP do endereço.")
	private String cep;
}
