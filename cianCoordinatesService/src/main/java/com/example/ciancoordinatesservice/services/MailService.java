package com.example.ciancoordinatesservice.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender emailSender;

    @Autowired
    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    private void sendSimpleEmail(String toAddress, String subject, String message) throws MessagingException {
        log.info(toAddress, subject, message);
        MimeMessage messageToSend = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messageToSend);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(message);
        emailSender.send(messageToSend);
        log.info("Email sent...");
    }

    @KafkaListener(topics = "commercialAdAdded")
    public void receiveCommercial(ConsumerRecord<String, String> consumerRecord) {
        String mail = consumerRecord.value();
        log.info("Received mail from commercialAdAdded topic: " + mail);
        try {
            sendSimpleEmail("maksinismile@gmail.com",
                    "Объявление успешно опубликовано!",
                    "Здравствуйте!\n" +
                            "Ваше объявление о продаже коммерческой недвижимости опубликовано.\n" +
                            "С уважением,\n" +
                            "Циан");
        } catch (MailException | MessagingException mailException) {
            log.error("Error while sending out email..{}", (Object) mailException.getStackTrace());
        }
    }

    @KafkaListener(topics = "residentialAdAdded")
    public void receiveResidential(ConsumerRecord<String, String> consumerRecord) {
        String mail = consumerRecord.value();
        log.info("Received mail from residentialAdAdded topic: " + mail);
        try {
            sendSimpleEmail(mail,
                    "Объявление успешно опубликовано!",
                    "Здравствуйте!\n" +
                            "Ваше объявление о продаже жилой недвижимости опубликовано.\n" +
                            "С уважением,\n" +
                            "Циан");
        } catch (MailException | MessagingException mailException) {
            log.error("Error while sending out email..{}", (Object) mailException.getStackTrace());
        }
    }


}
