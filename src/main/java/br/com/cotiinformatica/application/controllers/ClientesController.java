package br.com.cotiinformatica.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Object> criar(@RequestBody @Valid CriarClienteRequestDto dtoCliente) {
	
		List<String> errors = new ArrayList<>();
		validator.validate(dtoCliente.getEndereco()).forEach(violation -> {
			errors.add(violation.getMessage());
		});

		if (!errors.isEmpty()) {
			return ResponseEntity.status(400).body(errors);
		}

		ClienteResponseDto response = clienteDomainService.criarCliente(dtoCliente);
		return ResponseEntity.status(201).body(response);
	}
}
