package br.com.cotiinformatica.infrastructure.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import br.com.cotiinformatica.domain.dtos.EnviarEmailDto;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailSenderComponent {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;

    public void enviarEmail(EnviarEmailDto dto) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Aqui começa o HTML embutido
        String htmlMsg = "<html><head>" +
                         "<link rel='stylesheet' type='text/css' href='estilos.css'>" +
                         "</head><body>" +
                         "<div class='container'>" +
                         "<div class='header'><h2>" + dto.getAssunto() + "</h2></div>" +
                         "<div class='content'>" +
                         "<p>" + dto.getMensagem() + "</p>" +
                         "</div>" +
                         "<div class='footer'>Este é um e-mail automatizado enviado por nossa aplicação.</div>" +
                         "</div></body></html>";
        // Fim do HTML embutido

        helper.setFrom(userName);
        helper.setTo(dto.getEmailDest());
        helper.setSubject(dto.getAssunto());
        helper.setText(htmlMsg, true); // O segundo parâmetro true indica que o texto é HTML

        javaMailSender.send(mimeMessage);
    }
}