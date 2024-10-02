package org.deepaksharma.notificationservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static org.deepaksharma.notificationservice.constants.KafkaConstants.USER_CREATION_TOPIC;
import static org.deepaksharma.notificationservice.constants.UserCreationTopicConstants.EMAIL;
import static org.deepaksharma.notificationservice.constants.UserCreationTopicConstants.NAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCreationConsumer {
    private final ObjectMapper objectMapper;
    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = USER_CREATION_TOPIC, groupId = "notification_group")
    public void userCreated(String message) throws JsonProcessingException {
        log.info("Notification sent to user: {}", message);
        ObjectNode node = objectMapper.readValue(message, ObjectNode.class);
        String name = node.get(NAME).textValue();
        String email = node.get(EMAIL).textValue();

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("wallet-service@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Welcome to Wallet Service");
        mailMessage.setText("Hello " + name + ",\n\nWelcome to Wallet Service. We are excited to have you on board.");
        javaMailSender.send(mailMessage);
        log.info("Notification sent to user with email: {}", email);
    }
}
