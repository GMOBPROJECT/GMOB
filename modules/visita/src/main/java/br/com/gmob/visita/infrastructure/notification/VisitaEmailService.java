package br.com.gmob.visita.infrastructure.notification;

import br.com.gmob.visita.domain.event.VisitaAgendadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class VisitaEmailService implements VisitaNotificationService {

    private static final Logger log = LoggerFactory.getLogger(VisitaEmailService.class);

    private final JavaMailSender mailSender;

    public VisitaEmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarConfirmacaoVisita(VisitaAgendadaEvent event) {
        if (mailSender == null || event.clienteEmail() == null || event.clienteEmail().isBlank()) {
            log.info(
                    "Notificação de visita (modo log): cliente={}, email={}, imóvel={}, data={}, hora={}-{}",
                    event.clienteNome(),
                    event.clienteEmail(),
                    event.imovelEndereco(),
                    event.dataVisita(),
                    event.horaInicio(),
                    event.horaTermino()
            );
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.clienteEmail());
        message.setSubject("Visita agendada - GMOB");
        message.setText("""
                Olá, %s!

                Sua visita foi agendada com sucesso.

                Imóvel: %s
                Data: %s
                Horário: %s às %s
                Corretor: %s

                Observações: %s
                """.formatted(
                event.clienteNome(),
                event.imovelEndereco(),
                event.dataVisita(),
                event.horaInicio(),
                event.horaTermino(),
                event.corretorNome(),
                event.observacoes() != null ? event.observacoes() : "Nenhuma"
        ));
        mailSender.send(message);
        log.info("E-mail de confirmação de visita enviado para {}", event.clienteEmail());
    }
}
