package br.com.cotiinformatica.domain.services;

import java.util.List;
import java.util.UUID;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.interfaces.ClienteDomainService;

public class ClienteDomainServiceImpl implements ClienteDomainService {

	@Override
	public ClienteResponseDto criarCliente(CriarClienteRequestDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClienteResponseDto atualizarCliente(AtualizarClienteRequestDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClienteResponseDto deletarCliente(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClienteResponseDto obterCliente(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClienteResponseDto> consultarClientes() {
		// TODO Auto-generated method stub
		return null;
	}

}
