package br.com.cotiinformatica.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarEnderecoRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.entities.Endereco;
import br.com.cotiinformatica.domain.interfaces.ClienteDomainService;
import br.com.cotiinformatica.infrastructure.repositories.ClienteRepository;
import br.com.cotiinformatica.infrastructure.repositories.EnderecoRepository;

@Service
public class ClienteDomainServiceImpl implements ClienteDomainService {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public ClienteResponseDto criarCliente(CriarClienteRequestDto dtoCliente) {

		if (clienteRepository.findByCpf(dtoCliente.getCpf()).isPresent())
			throw new ResponseStatusException(HttpStatusCode.valueOf(422), "O CPF informado já está cadastrado.");		
		
		Cliente cliente = modelMapper.map(dtoCliente, Cliente.class);
		cliente.setId(UUID.randomUUID());
		
		Endereco endereco = modelMapper.map(dtoCliente.getEndereco(), Endereco.class);
		endereco.setId(UUID.randomUUID());
		endereco.setCliente(cliente);

		clienteRepository.save(cliente);
		enderecoRepository.save(endereco);

		cliente.setEnderecos(new ArrayList<Endereco>());
		cliente.getEnderecos().add(endereco);

		ClienteResponseDto response = modelMapper.map(cliente, ClienteResponseDto.class);
		return response;
	}

	@Override
	public ClienteResponseDto atualizarCliente(AtualizarClienteRequestDto dtoCliente) {

		if (clienteRepository.findById(dtoCliente.getId()).isEmpty())
			throw new IllegalArgumentException("Não foi possível encontrar um cliente com o ID informado.");

		AtualizarEnderecoRequestDto dtoEndereco = new AtualizarEnderecoRequestDto();
		Optional<Endereco> optionalEndereco = enderecoRepository.findById(dtoEndereco.getId());

		if (optionalEndereco.isPresent()) {

			if (optionalEndereco.get().getCliente().getId().equals(dtoCliente.getId()))
				throw new IllegalArgumentException("O ID do endereço informado pertence a outro cliente.");
		}

		Optional<Cliente> optionalCliente = clienteRepository.findByCpf(dtoCliente.getCpf());

		if (optionalCliente.isPresent()) {

			if (!(optionalCliente.get().getId().equals(dtoCliente.getId())))
				throw new IllegalArgumentException("O CPF informado pertence a outro cliente.");
		}

		Cliente cliente = modelMapper.map(dtoCliente, Cliente.class);
		Endereco endereco = modelMapper.map(dtoEndereco, Endereco.class);

		cliente.getEnderecos().add(endereco);
		clienteRepository.save(cliente);

		ClienteResponseDto response = modelMapper.map(cliente, ClienteResponseDto.class);
		return response;
	}

	@Override
	public ClienteResponseDto deletarCliente(UUID id) {

		Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente.isEmpty())
			throw new IllegalArgumentException("O ID informado não corresponde a um cliente.");

		ClienteResponseDto response = modelMapper.map(cliente.get(), ClienteResponseDto.class);

		clienteRepository.delete(cliente.get());

		return response;
	}

	@Override
	public ClienteResponseDto obterCliente(UUID id) {

		Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente.isEmpty())
			throw new IllegalArgumentException("O ID informado não corresponde a um cliente.");

		ClienteResponseDto response = modelMapper.map(cliente.get(), ClienteResponseDto.class);
		return response;
	}

	@Override
	public List<ClienteResponseDto> consultarClientes() {

		List<Cliente> clientes = clienteRepository.findAll();

		List<ClienteResponseDto> response = modelMapper.map(clientes, new TypeToken<List<ClienteResponseDto>>() {
		}.getType());
		return response;
	}

}
