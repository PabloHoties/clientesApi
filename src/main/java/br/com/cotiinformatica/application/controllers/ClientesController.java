package br.com.cotiinformatica.application.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.interfaces.ClienteDomainService;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClientesController {

	@Autowired
	private ClienteDomainService clienteDomainService;

	@Autowired
	private Validator validator;

	@PostMapping("criar")
	public ResponseEntity<Object> criar(@RequestBody @Valid CriarClienteRequestDto dto) throws Exception {
	
		List<String> errors = new ArrayList<>();
		validator.validate(dto.getEndereco()).forEach(violation -> {
			errors.add(violation.getMessage());
		});

		if (!errors.isEmpty()) {
			return ResponseEntity.status(400).body(errors);
		}

		ClienteResponseDto response = clienteDomainService.criarCliente(dto);
		return ResponseEntity.status(201).body(response);
	}
	
	@PutMapping("atualizar")
	public ResponseEntity<Object> atualizar(@RequestBody @Valid AtualizarClienteRequestDto dto) {
		
		List<String> errors = new ArrayList<>();
		validator.validate(dto.getEndereco()).forEach(violation -> {
			errors.add(violation.getMessage());
		});

		if (!errors.isEmpty()) {
			return ResponseEntity.status(400).body(errors);
		}
		
		ClienteResponseDto response = clienteDomainService.atualizarCliente(dto);
		return ResponseEntity.status(200).body(response);
	}
	
	@DeleteMapping("deletar/{id}")
	public ResponseEntity<ClienteResponseDto> deletar(@PathVariable("id") UUID id) {
		
		ClienteResponseDto response = clienteDomainService.deletarCliente(id);
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("obter/{id}")
	public ResponseEntity<ClienteResponseDto> obter(@PathVariable("id") UUID id) {
		
		ClienteResponseDto response = clienteDomainService.obterCliente(id);
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("consultar")
	public ResponseEntity<List<ClienteResponseDto>> consultar() {
		
		List<ClienteResponseDto> response = clienteDomainService.consultarClientes();
		return ResponseEntity.status(200).body(response);
	}
}
