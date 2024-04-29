package br.com.cotiinformatica.domain.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;

public interface ClienteDomainService {

	ClienteResponseDto criarCliente(CriarClienteRequestDto dto);
	
	ClienteResponseDto atualizarCliente(AtualizarClienteRequestDto dto);
	
	ClienteResponseDto deletarCliente(UUID id);
	
	ClienteResponseDto obterCliente(UUID id);
	
	List<ClienteResponseDto> consultarClientes();
}
