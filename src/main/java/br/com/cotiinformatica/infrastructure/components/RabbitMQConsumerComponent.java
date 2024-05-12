package br.com.cotiinformatica.infrastructure.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.domain.dtos.EnviarEmailDto;
import br.com.cotiinformatica.domain.entities.Cliente;

@Component
public class RabbitMQConsumerComponent {

	@Autowired
	private MailSenderComponent mailSenderComponent;

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = { "${queue.name}" })
	public void receive(@Payload String message) {

		try {
			Cliente cliente = objectMapper.readValue(message, Cliente.class);

			EnviarEmailDto dto = new EnviarEmailDto();
			dto.setEmailDest(cliente.getEmail());
			dto.setAssunto("Boas-vindas à clientesApi!");
			dto.setMensagem("<p class=\"welcome-message\">Olá " + cliente.getNome() + "!<br> É com grande prazer que lhe damos as boas-vindas à clientesApi!<br><br> Estamos muito felizes em informar que seu cadastro foi concluído com sucesso.<br><br>Atenciosamente,<br>clientesApi</p>");


			mailSenderComponent.enviarEmail(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
