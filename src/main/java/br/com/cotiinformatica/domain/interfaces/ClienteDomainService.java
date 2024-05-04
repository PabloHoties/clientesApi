package br.com.cotiinformatica.domain.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;

public interface ClienteDomainService {

	ClienteResponseDto criarCliente(CriarClienteRequestDto dtoCliente);
	
	ClienteResponseDto atualizarCliente(AtualizarClienteRequestDto dtoCliente);
	
	ClienteResponseDto deletarCliente(UUID id);
	
	ClienteResponseDto obterCliente(UUID id);
	
	List<ClienteResponseDto> consultarClientes();
}
