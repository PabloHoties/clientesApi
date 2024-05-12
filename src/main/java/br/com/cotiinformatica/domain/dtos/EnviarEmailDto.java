package br.com.cotiinformatica.domain.dtos;

import lombok.Data;

@Data
public class EnviarEmailDto {

	private String emailDest;
	private String assunto;
	private String mensagem;
}
