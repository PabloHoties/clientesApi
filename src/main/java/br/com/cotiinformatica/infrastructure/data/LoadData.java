package br.com.cotiinformatica.infrastructure.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.entities.Endereco;
import br.com.cotiinformatica.infrastructure.repositories.ClienteRepository;
import br.com.cotiinformatica.infrastructure.repositories.EnderecoRepository;

@Component
public class LoadData implements ApplicationRunner {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	EnderecoRepository enderecoRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		Cliente cliente = new Cliente();
		cliente.setId(UUID.randomUUID());
		cliente.setNome("Administrator");
		cliente.setEmail("admin@administration.com");
		cliente.setCpf("00000000000");
		Date dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse("02/02/2020");
		cliente.setDataNascimento(dataNascimento);

		Endereco endereco = new Endereco();
		endereco.setId(UUID.randomUUID());
		endereco.setLogradouro("Av. Um, Dois, Três.");
		endereco.setComplemento("Lt 0', Qd.02");
		endereco.setNumero("00");
		endereco.setBairro("Quatro");
		endereco.setCidade("Cinco");
		endereco.setUf("AA");
		endereco.setCep("00000-000");
		endereco.setCliente(cliente);

		Optional<Cliente> clienteOpt = clienteRepository.findByCpf(cliente.getCpf());

		if (clienteOpt.isEmpty()) {
			clienteRepository.save(cliente);
			enderecoRepository.save(endereco);
		}

	}

}
