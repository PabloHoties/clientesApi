package br.com.cotiinformatica;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
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
import br.com.cotiinformatica.domain.dtos.EnderecoResponseDto;

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

	@Test
	@Order(1)
	public void criarClienteComSucessoTest() throws Exception {

		Faker faker = new Faker();

		CriarClienteRequestDto cliente = new CriarClienteRequestDto();
		cliente.setNome(faker.name().fullName());
		cliente.setEmail(faker.internet().emailAddress());
		cliente.setCpf(faker.number().digits(11));
		cliente.setDataNascimento(faker.date().birthday());
		
		CriarEnderecoRequestDto endereco = new CriarEnderecoRequestDto();
		endereco.setLogradouro(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{10,100}$"));
		endereco.setComplemento(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{5,25}$"));
		endereco.setNumero(faker.regexify("^\\d{1,5}$"));
		endereco.setBairro(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setCidade(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setUf(faker.regexify("^[A-Z]{2}$"));
		endereco.setCep(faker.regexify("^\\d{5}-\\d{3}"));
		
		cliente.getEnderecos().add(endereco);

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(cliente))).andExpectAll(status().isCreated()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		ClienteResponseDto clienteResponse = objectMapper.readValue(content, ClienteResponseDto.class);
		assertNotNull(clienteResponse.getId());
		assertEquals(clienteResponse.getNome(), cliente.getNome());
		assertEquals(clienteResponse.getEmail(), cliente.getEmail());
		assertEquals(clienteResponse.getCpf(), cliente.getCpf());
		assertEquals(clienteResponse.getDataNascimento(), cliente.getDataNascimento());
		
		EnderecoResponseDto enderecoResponse = objectMapper.readValue(content, EnderecoResponseDto.class);
		assertNotNull(enderecoResponse.getId());
		assertEquals(enderecoResponse.getLogradouro(), endereco.getLogradouro());
		assertEquals(enderecoResponse.getComplemento(), endereco.getComplemento());
		assertEquals(enderecoResponse.getNumero(), endereco.getNumero());
		assertEquals(enderecoResponse.getBairro(), endereco.getBairro());
		assertEquals(enderecoResponse.getCidade(), endereco.getCidade());
		assertEquals(enderecoResponse.getUf(), endereco.getUf());
		assertEquals(enderecoResponse.getCep(), endereco.getCep());

		cpf = cliente.getCpf();
		idCliente = clienteResponse.getId();
		idEndereco = enderecoResponse.getId();
	}

	@Test
	@Order(2)
	public void criarClienteComCpfInvalidoTest() throws Exception {		
		
		Faker faker = new Faker();
		
		CriarClienteRequestDto cliente = new CriarClienteRequestDto();
		cliente.setNome(faker.name().fullName());
		cliente.setEmail(faker.internet().emailAddress());
		cliente.setCpf(cpf);
		cliente.setDataNascimento(faker.date().birthday());
		
		CriarEnderecoRequestDto endereco = new CriarEnderecoRequestDto();
		endereco.setLogradouro(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{10,100}$"));
		endereco.setComplemento(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{5,25}$"));
		endereco.setNumero(faker.regexify("^\\d{1,5}$"));
		endereco.setBairro(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setCidade(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setUf(faker.regexify("^[A-Z]{2}$"));
		endereco.setCep(faker.regexify("^\\d{5}-\\d{3}"));
		
		cliente.getEnderecos().add(endereco);
		
		MvcResult result = mockMvc.perform
				(post("/api/clientes/criar")
				.contentType("application/json")
				.content(objectMapper
						.writeValueAsString(cliente)))
				.andExpectAll(status().isBadRequest())
				.andReturn();
		
		String content = result.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains(""));
	}

	@Test
	@Order(3)
	public void criarClienteComDadosInvalidosTest() throws Exception {

		CriarClienteRequestDto cliente = new CriarClienteRequestDto();
		cliente.setNome("");
		cliente.setEmail("");
		cliente.setCpf("");
		cliente.setDataNascimento(null);
		
		CriarEnderecoRequestDto endereco = new CriarEnderecoRequestDto();
		endereco.setLogradouro("");
		endereco.setComplemento("");
		endereco.setNumero("");
		endereco.setBairro("");
		endereco.setCidade("");
		endereco.setUf("");
		endereco.setCep("");

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(cliente))).andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains(""));
	}

	@Test
	@Order(4)
	public void atualizarClienteComSucessoTest() throws Exception {
		
		Faker faker = new Faker();

		AtualizarClienteRequestDto cliente = new AtualizarClienteRequestDto();
		cliente.setId(idCliente);
		cliente.setNome(faker.name().fullName());
		cliente.setEmail(faker.internet().emailAddress());
		cliente.setCpf(faker.number().digits(11));
		cliente.setDataNascimento(faker.date().birthday());
		
		AtualizarEnderecoRequestDto endereco = new AtualizarEnderecoRequestDto();
		endereco.setId(idEndereco);
		endereco.setLogradouro(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{10,100}$"));
		endereco.setComplemento(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{5,25}$"));
		endereco.setNumero(faker.regexify("^\\d{1,5}$"));
		endereco.setBairro(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setCidade(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setUf(faker.regexify("^[A-Z]{2}$"));
		endereco.setCep(faker.regexify("^\\d{5}-\\d{3}"));
		
		cliente.getEnderecos().add(endereco);

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(cliente))).andExpectAll(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		ClienteResponseDto clienteResponse = objectMapper.readValue(content, ClienteResponseDto.class);
		assertNotNull(clienteResponse.getId());
		assertEquals(clienteResponse.getNome(), cliente.getNome());
		assertEquals(clienteResponse.getEmail(), cliente.getEmail());
		assertEquals(clienteResponse.getCpf(), cliente.getCpf());
		assertEquals(clienteResponse.getDataNascimento(), cliente.getDataNascimento());
		
		EnderecoResponseDto enderecoResponse = objectMapper.readValue(content, EnderecoResponseDto.class);
		assertNotNull(enderecoResponse.getId());
		assertEquals(enderecoResponse.getLogradouro(), endereco.getLogradouro());
		assertEquals(enderecoResponse.getComplemento(), endereco.getComplemento());
		assertEquals(enderecoResponse.getNumero(), endereco.getNumero());
		assertEquals(enderecoResponse.getBairro(), endereco.getBairro());
		assertEquals(enderecoResponse.getCidade(), endereco.getCidade());
		assertEquals(enderecoResponse.getUf(), endereco.getUf());
		assertEquals(enderecoResponse.getCep(), endereco.getCep());
	}

	@Test
	@Order(5)
	public void atualizarClienteComIdInvalidoTest() throws Exception {
		
		Faker faker = new Faker();

		AtualizarClienteRequestDto cliente = new AtualizarClienteRequestDto();
		cliente.setId(UUID.randomUUID());
		cliente.setNome(faker.name().fullName());
		cliente.setEmail(faker.internet().emailAddress());
		cliente.setCpf(faker.number().digits(11));
		cliente.setDataNascimento(faker.date().birthday());
		
		AtualizarEnderecoRequestDto endereco = new AtualizarEnderecoRequestDto();
		endereco.setId(UUID.randomUUID());
		endereco.setLogradouro(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{10,100}$"));
		endereco.setComplemento(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{5,25}$"));
		endereco.setNumero(faker.regexify("^\\d{1,5}$"));
		endereco.setBairro(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setCidade(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setUf(faker.regexify("^[A-Z]{2}$"));
		endereco.setCep(faker.regexify("^\\d{5}-\\d{3}"));
		
		cliente.getEnderecos().add(endereco);

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(cliente))).andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains(""));
		assertTrue(content.contains(""));
	}

	@Test
	@Order(6)
	public void atualizarClienteComCpfInvalidoTest() throws Exception {
		
		Faker faker = new Faker();
		
		//Criando um novo cliente.
		CriarClienteRequestDto novoCliente = new CriarClienteRequestDto();
		novoCliente.setNome(faker.name().fullName());
		novoCliente.setEmail(faker.internet().emailAddress());
		novoCliente.setCpf(faker.number().digits(11));
		novoCliente.setDataNascimento(faker.date().birthday());
		
		CriarEnderecoRequestDto novoEndereco = new CriarEnderecoRequestDto();
		novoEndereco.setLogradouro(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{10,100}$"));
		novoEndereco.setComplemento(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{5,25}$"));
		novoEndereco.setNumero(faker.regexify("^\\d{1,5}$"));
		novoEndereco.setBairro(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		novoEndereco.setCidade(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		novoEndereco.setUf(faker.regexify("^[A-Z]{2}$"));
		novoEndereco.setCep(faker.regexify("^\\d{5}-\\d{3}"));
		
		novoCliente.getEnderecos().add(novoEndereco);

		MvcResult novoResult = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(novoCliente))).andReturn();

		String novoContent = novoResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		ClienteResponseDto clienteResponse = objectMapper.readValue(novoContent, ClienteResponseDto.class);
		
		String cpfNovo = clienteResponse.getCpf();
		
		//Atualizando um cliente com um CPF de outro cliente.
		AtualizarClienteRequestDto cliente = new AtualizarClienteRequestDto();
		cliente.setId(idCliente);
		cliente.setNome(faker.name().fullName());
		cliente.setEmail(faker.internet().emailAddress());
		cliente.setCpf(cpfNovo);
		cliente.setDataNascimento(faker.date().birthday());
		
		AtualizarEnderecoRequestDto endereco = new AtualizarEnderecoRequestDto();
		endereco.setId(idEndereco);
		endereco.setLogradouro(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{10,100}$"));
		endereco.setComplemento(faker.regexify("^[a-zA-ZÀ-ÿ0-9\s]{5,25}$"));
		endereco.setNumero(faker.regexify("^\\d{1,5}$"));
		endereco.setBairro(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setCidade(faker.regexify("^[a-zA-ZÀ-ÿ\s]{3,25}$"));
		endereco.setUf(faker.regexify("^[A-Z]{2}$"));
		endereco.setCep(faker.regexify("^\\d{5}-\\d{3}"));
		
		cliente.getEnderecos().add(endereco);

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(cliente))).andExpectAll(status().isBadRequest()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains(""));
	}

	@Test
	@Order(7)
	public void atualizarClienteComDadosInvalidosTest() throws Exception {

		AtualizarClienteRequestDto cliente = new AtualizarClienteRequestDto();
		cliente.setId(null);
		cliente.setNome("");
		cliente.setEmail("");
		cliente.setCpf("");
		cliente.setDataNascimento(null);
		
		AtualizarEnderecoRequestDto endereco = new AtualizarEnderecoRequestDto();
		endereco.setId(null);
		endereco.setLogradouro("");
		endereco.setComplemento("");
		endereco.setNumero("");
		endereco.setBairro("");
		endereco.setCidade("");
		endereco.setUf("");
		endereco.setCep("");
		
		cliente.getEnderecos().add(endereco);

		MvcResult result = mockMvc.perform(post("/api/clientes/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(cliente))).andExpectAll(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		assertTrue(content.contains(""));
	}

	@Test
	@Order(8)
	public void deletarClienteComSucessoTest() throws Exception {
		fail("Teste não implementado.");
	}

	@Test
	@Order(9)
	public void deletarClienteComIdInvalidoTest() throws Exception {
		fail("Teste não implementado.");
	}

	@Test
	@Order(10)
	public void obterClienteComSucessoTest() throws Exception {
		fail("Teste não implementado.");
	}

	@Test
	@Order(11)
	public void obterClienteComIdInvalidoTest() throws Exception {
		fail("Teste não implementado.");
	}

	@Test
	@Order(12)
	public void consultarClientesComSucessoTest() throws Exception {
		fail("Teste não implementado.");
	}
}
