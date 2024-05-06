package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.domain.dtos.AtualizarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.AtualizarEnderecoRequestDto;
import br.com.cotiinformatica.domain.dtos.ClienteResponseDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteRequestDto;
import br.com.cotiinformatica.domain.dtos.CriarEnderecoRequestDto;
import br.com.cotiinformatica.domain.entities.Cliente;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientesTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	static String cpf;
	static UUID idCliente;
	static UUID idEndereco;
	static UUID novoIdCliente;

	@Test
	@Order(1)
	public void criarClienteComSucessoTest() throws Exception {

		Faker faker = new Faker();

		CriarClienteRequestDto dto = new CriarClienteRequestDto();
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(faker.number().digits(11));
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new CriarEnderecoRequestDto());
		dto.getEndereco().setLogradouro(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{10,100}"));
		dto.getEndereco().setComplemento(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{5,25}"));
		dto.getEndereco().setNumero(faker.regexify("\\d{1,5}"));
		dto.getEndereco().setBairro(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setCidade(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setUf(faker.regexify("[A-Z]{2}"));
		dto.getEndereco().setCep(faker.regexify("\\d{5}-\\d{3}"));

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(dto))).andExpectAll(status().isCreated()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		ClienteResponseDto clienteResponse = objectMapper.readValue(content, ClienteResponseDto.class);
		assertNotNull(clienteResponse.getId());
		assertEquals(clienteResponse.getNome(), dto.getNome());
		assertEquals(clienteResponse.getEmail(), dto.getEmail());
		assertEquals(clienteResponse.getCpf(), dto.getCpf());
		assertEquals(clienteResponse.getDataNascimento(), dto.getDataNascimento());

		assertNotNull(clienteResponse.getEnderecos().get(0).getId());
		assertEquals(clienteResponse.getEnderecos().get(0).getLogradouro(), dto.getEndereco().getLogradouro());
		assertEquals(clienteResponse.getEnderecos().get(0).getComplemento(), dto.getEndereco().getComplemento());
		assertEquals(clienteResponse.getEnderecos().get(0).getNumero(), dto.getEndereco().getNumero());
		assertEquals(clienteResponse.getEnderecos().get(0).getBairro(), dto.getEndereco().getBairro());
		assertEquals(clienteResponse.getEnderecos().get(0).getCidade(), dto.getEndereco().getCidade());
		assertEquals(clienteResponse.getEnderecos().get(0).getUf(), dto.getEndereco().getUf());
		assertEquals(clienteResponse.getEnderecos().get(0).getCep(), dto.getEndereco().getCep());

		cpf = dto.getCpf();
		idCliente = clienteResponse.getId();
		idEndereco = clienteResponse.getEnderecos().get(0).getId();
	}

	@Test
	@Order(2)
	public void criarClienteComCpfInvalidoTest() throws Exception {

		Faker faker = new Faker();

		CriarClienteRequestDto dto = new CriarClienteRequestDto();
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(cpf);
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new CriarEnderecoRequestDto());
		dto.getEndereco().setLogradouro(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{10,100}"));
		dto.getEndereco().setComplemento(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{5,25}"));
		dto.getEndereco().setNumero(faker.regexify("\\d{1,5}"));
		dto.getEndereco().setBairro(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setCidade(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setUf(faker.regexify("[A-Z]{2}"));
		dto.getEndereco().setCep(faker.regexify("\\d{5}-\\d{3}"));

		MvcResult result = mockMvc
				.perform(post("/api/clientes/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isUnprocessableEntity()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("O CPF informado já está cadastrado."));
	}

	@Test
	@Order(3)
	public void criarClienteComDadosDoClienteInvalidosTest() throws Exception {

		CriarClienteRequestDto dto = new CriarClienteRequestDto();
		dto.setNome("");
		dto.setEmail("");
		dto.setCpf("");
		dto.setDataNascimento(null);

		MvcResult result = mockMvc
				.perform(post("/api/clientes/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains("nome: Por favor, informe o nome do cliente."));
		assertTrue(content.contains("nome: Por favor, insira um nome válido."));
		assertTrue(content.contains("email: Por favor, informe o email do cliente."));
		assertTrue(content.contains("cpf: Por favor, informe o CPF do cliente."));
		assertTrue(content.contains("cpf: Por favor, insira um CPF contendo apenas 11 dígitos."));
		assertTrue(content.contains("dataNascimento: Por favor, informe a data de nascimento do cliente."));
		assertTrue(content.contains("endereco: Por favor, informe as informações do endereço do cliente."));
	}

	@Test
	@Order(4)
	public void criarClienteComDadosDoEnderecoInvalidosTest() throws Exception {

		Faker faker = new Faker();

		CriarClienteRequestDto dto = new CriarClienteRequestDto();
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(faker.number().digits(11));
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new CriarEnderecoRequestDto());
		dto.getEndereco().setLogradouro("");
		dto.getEndereco().setComplemento("");
		dto.getEndereco().setNumero("");
		dto.getEndereco().setBairro("");
		dto.getEndereco().setCidade("");
		dto.getEndereco().setUf("");
		dto.getEndereco().setCep("");

		MvcResult result = mockMvc
				.perform(post("/api/clientes/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains("Por favor, informe o logradouro do cliente."));
		assertTrue(content.contains("Por favor, informe um logradouro válido."));
		assertTrue(content.contains("Por favor, informe o complemento do endereço."));
		assertTrue(content.contains("Por favor, informe um complemento válido."));
		assertTrue(content.contains("Por favor, informe o número do endereço."));
		assertTrue(content.contains("Por favor, informe um número válido."));
		assertTrue(content.contains("Por favor, informe o bairro do cliente."));
		assertTrue(content.contains("Por favor, informe um bairro válido."));
		assertTrue(content.contains("Por favor, informe a cidade do cliente."));
		assertTrue(content.contains("Por favor, informe uma cidade válida."));
		assertTrue(content.contains("Por favor, informe a sigla do estado."));
		assertTrue(content.contains("Por favor, informe uma sigla com os caracteres maiúsculos."));
		assertTrue(content.contains("Por favor, informe o CEP do endereço."));
		assertTrue(content.contains("Por favor, informe um CEP no formato '12345-678'."));
	}

	@Test
	@Order(5)
	public void atualizarClienteComSucessoTest() throws Exception {

		Faker faker = new Faker();

		AtualizarClienteRequestDto dto = new AtualizarClienteRequestDto();
		dto.setId(idCliente);
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(faker.number().digits(11));
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new AtualizarEnderecoRequestDto());
		dto.getEndereco().setId(idEndereco);
		dto.getEndereco().setLogradouro(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{10,100}"));
		dto.getEndereco().setComplemento(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{5,25}"));
		dto.getEndereco().setNumero(faker.regexify("\\d{1,5}"));
		dto.getEndereco().setBairro(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setCidade(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setUf(faker.regexify("[A-Z]{2}"));
		dto.getEndereco().setCep(faker.regexify("\\d{5}-\\d{3}"));

		MvcResult result = mockMvc.perform(put("/api/clientes/atualizar").contentType("application/json")
				.content(objectMapper.writeValueAsString(dto))).andExpectAll(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		ClienteResponseDto clienteResponse = objectMapper.readValue(content, ClienteResponseDto.class);
		assertNotNull(clienteResponse.getId());
		assertEquals(clienteResponse.getNome(), dto.getNome());
		assertEquals(clienteResponse.getEmail(), dto.getEmail());
		assertEquals(clienteResponse.getCpf(), dto.getCpf());
		assertEquals(clienteResponse.getDataNascimento(), dto.getDataNascimento());

		assertNotNull(clienteResponse.getEnderecos().get(0).getId());
		assertEquals(clienteResponse.getEnderecos().get(0).getLogradouro(), dto.getEndereco().getLogradouro());
		assertEquals(clienteResponse.getEnderecos().get(0).getComplemento(), dto.getEndereco().getComplemento());
		assertEquals(clienteResponse.getEnderecos().get(0).getNumero(), dto.getEndereco().getNumero());
		assertEquals(clienteResponse.getEnderecos().get(0).getBairro(), dto.getEndereco().getBairro());
		assertEquals(clienteResponse.getEnderecos().get(0).getCidade(), dto.getEndereco().getCidade());
		assertEquals(clienteResponse.getEnderecos().get(0).getUf(), dto.getEndereco().getUf());
		assertEquals(clienteResponse.getEnderecos().get(0).getCep(), dto.getEndereco().getCep());
	}

	@Test
	@Order(6)
	public void atualizarClienteComIdDoClienteInvalidoTest() throws Exception {

		Faker faker = new Faker();

		AtualizarClienteRequestDto dto = new AtualizarClienteRequestDto();
		dto.setId(UUID.randomUUID());
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(faker.number().digits(11));
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new AtualizarEnderecoRequestDto());
		dto.getEndereco().setId(idEndereco);
		dto.getEndereco().setLogradouro(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{10,100}"));
		dto.getEndereco().setComplemento(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{5,25}"));
		dto.getEndereco().setNumero(faker.regexify("\\d{1,5}"));
		dto.getEndereco().setBairro(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setCidade(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setUf(faker.regexify("[A-Z]{2}"));
		dto.getEndereco().setCep(faker.regexify("\\d{5}-\\d{3}"));

		MvcResult result = mockMvc
				.perform(put("/api/clientes/atualizar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("Não foi possível encontrar um cliente com o ID informado."));
	}

	@Test
	@Order(7)
	public void atualizarClienteComIdDoEnderecoInvalidoTest() throws Exception {

		Faker faker = new Faker();

		AtualizarClienteRequestDto dto = new AtualizarClienteRequestDto();
		dto.setId(idCliente);
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(faker.number().digits(11));
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new AtualizarEnderecoRequestDto());
		dto.getEndereco().setId(UUID.randomUUID());
		dto.getEndereco().setLogradouro(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{10,100}"));
		dto.getEndereco().setComplemento(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{5,25}"));
		dto.getEndereco().setNumero(faker.regexify("\\d{1,5}"));
		dto.getEndereco().setBairro(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setCidade(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setUf(faker.regexify("[A-Z]{2}"));
		dto.getEndereco().setCep(faker.regexify("\\d{5}-\\d{3}"));

		MvcResult result = mockMvc
				.perform(put("/api/clientes/atualizar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("Não foi possível encontrar um endereço com o ID informado."));
	}

	@Test
	@Order(8)
	public void atualizarClienteComCpfInvalidoTest() throws Exception {

		Faker faker = new Faker();

		AtualizarClienteRequestDto dto = new AtualizarClienteRequestDto();
		dto.setId(idCliente);
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf("00000000000");
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new AtualizarEnderecoRequestDto());
		dto.getEndereco().setId(idEndereco);
		dto.getEndereco().setLogradouro(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{10,100}"));
		dto.getEndereco().setComplemento(faker.regexify("[a-zA-ZÀ-ÿ0-9\s]{5,25}"));
		dto.getEndereco().setNumero(faker.regexify("\\d{1,5}"));
		dto.getEndereco().setBairro(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setCidade(faker.regexify("[a-zA-ZÀ-ÿ\s]{3,25}"));
		dto.getEndereco().setUf(faker.regexify("[A-Z]{2}"));
		dto.getEndereco().setCep(faker.regexify("\\d{5}-\\d{3}"));

		MvcResult result = mockMvc
				.perform(put("/api/clientes/atualizar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isUnprocessableEntity()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("O CPF informado pertence a outro cliente."));
	}

	@Test
	@Order(9)
	public void atualizarClienteComDadosDoClienteInvalidosTest() throws Exception {

		AtualizarClienteRequestDto dto = new AtualizarClienteRequestDto();
		dto.setId(null);
		dto.setNome("");
		dto.setEmail("");
		dto.setCpf("");
		dto.setDataNascimento(null);

		MvcResult result = mockMvc
				.perform(put("/api/clientes/atualizar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains("id: Por favor, informe o ID do cliente."));
		assertTrue(content.contains("nome: Por favor, informe o nome do cliente."));
		assertTrue(content.contains("nome: Por favor, insira um nome válido."));
		assertTrue(content.contains("email: Por favor, informe o email do cliente."));
		assertTrue(content.contains("cpf: Por favor, informe o CPF do cliente."));
		assertTrue(content.contains("cpf: Por favor, insira um CPF contendo apenas 11 dígitos."));
		assertTrue(content.contains("dataNascimento: Por favor, informe a data de nascimento do cliente."));
		assertTrue(content.contains("endereco: Por favor, informe as informações do endereço do cliente."));
	}

	@Test
	@Order(10)
	public void atualizarClienteComDadosDoEnderecoInvalidosTest() throws Exception {

		Faker faker = new Faker();

		AtualizarClienteRequestDto dto = new AtualizarClienteRequestDto();
		dto.setId(UUID.randomUUID());
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setCpf(faker.number().digits(11));
		dto.setDataNascimento(faker.date().birthday());

		dto.setEndereco(new AtualizarEnderecoRequestDto());
		dto.getEndereco().setId(null);
		dto.getEndereco().setLogradouro("");
		dto.getEndereco().setComplemento("");
		dto.getEndereco().setNumero("");
		dto.getEndereco().setBairro("");
		dto.getEndereco().setCidade("");
		dto.getEndereco().setUf("");
		dto.getEndereco().setCep("");

		MvcResult result = mockMvc
				.perform(put("/api/clientes/atualizar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		assertTrue(content.contains("Por favor, informe o ID do endereço."));
		assertTrue(content.contains("Por favor, informe o logradouro do cliente."));
		assertTrue(content.contains("Por favor, informe um logradouro válido."));
		assertTrue(content.contains("Por favor, informe o complemento do endereço."));
		assertTrue(content.contains("Por favor, informe um complemento válido."));
		assertTrue(content.contains("Por favor, informe o número do endereço."));
		assertTrue(content.contains("Por favor, informe um número válido."));
		assertTrue(content.contains("Por favor, informe o bairro do cliente."));
		assertTrue(content.contains("Por favor, informe um bairro válido."));
		assertTrue(content.contains("Por favor, informe a cidade do cliente."));
		assertTrue(content.contains("Por favor, informe uma cidade válida."));
		assertTrue(content.contains("Por favor, informe a sigla do estado."));
		assertTrue(content.contains("Por favor, informe uma sigla com os caracteres maíusculos."));
		assertTrue(content.contains("Por favor, informe o CEP do endereço."));
		assertTrue(content.contains("Por favor, informe um CEP no formato '12345-678'."));
	}

	@Test
	@Order(11)
	public void obterClienteComSucessoTest() throws Exception {

		MvcResult result = mockMvc.perform(get("/api/clientes/obter/" + idCliente).contentType("application/json"))
				.andExpectAll(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains((idCliente).toString()));
	}

	@Test
	@Order(12)
	public void obterClienteComIdInvalidoTest() throws Exception {

		MvcResult result = mockMvc
				.perform(get("/api/clientes/obter/" + UUID.randomUUID().toString()).contentType("application/json"))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("O ID informado não corresponde a um cliente."));
	}

	@Test
	@Order(13)
	public void consultarClientesComSucessoTest() throws Exception {
		
		List<Cliente> clientes = new ArrayList<Cliente>();

		MvcResult result = mockMvc.perform(get("/api/clientes/consultar").contentType("application/json")
				.content(objectMapper.writeValueAsString(clientes))).andExpectAll(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains((idCliente).toString()));
	}

	@Test
	@Order(14)
	public void deletarClienteComSucessoTest() throws Exception {

		MvcResult result = mockMvc
				.perform(delete("/api/clientes/deletar/{id}", idCliente.toString()).contentType("application/json"))
				.andExpectAll(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains((idCliente).toString()));
		assertTrue(content.contains((idEndereco).toString()));
	}

	@Test
	@Order(15)
	public void deletarClienteComIdInvalidoTest() throws Exception {

		MvcResult result = mockMvc
				.perform(delete("/api/clientes/deletar/{id}", idCliente.toString()).contentType("application/json"))
				.andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains("O ID informado não corresponde a um cliente."));
	}

}
