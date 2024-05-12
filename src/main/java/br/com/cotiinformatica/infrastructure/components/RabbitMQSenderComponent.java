package br.com.cotiinformatica.infrastructure.components;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.domain.entities.Cliente;

@Component
public class RabbitMQSenderComponent {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public void sendMessage(Cliente cliente) throws Exception {
		String message = objectMapper.writeValueAsString(cliente);
		
		rabbitTemplate.convertAndSend(this.queue.getName(), message);
	}
}
