package br.com.cotiinformatica.domain.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarEnderecoRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.CriarEnderecoRequestDto;

public interface ClienteDomainService {

	ClienteResponseDto criarCliente(CriarClienteRequestDto dtoCliente, CriarEnderecoRequestDto dtoEndereco);
	
	ClienteResponseDto atualizarCliente(AtualizarClienteRequestDto dtoCliente, AtualizarEnderecoRequestDto dtoEndereco);
	
	ClienteResponseDto deletarCliente(UUID id);
	
	ClienteResponseDto obterCliente(UUID id);
	
	List<ClienteResponseDto> consultarClientes();
}
