package org.deepaksharma.walletservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deepaksharma.walletservice.model.Wallet;
import org.deepaksharma.walletservice.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.deepaksharma.walletservice.constants.KafkaConstants.USER_CREATION_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCreationConsumer {

    private final ObjectMapper objectMapper;

    private final WalletRepository walletRepository;

    @Value("${wallet.initial.amount}")
    Double initialWalletAmount;

    @KafkaListener(topics = USER_CREATION_TOPIC, groupId = "wallet-group")
    public void userCreated(String message) throws JsonProcessingException {
        log.info("User creation message received : {}", message);

        ObjectNode objectNode = objectMapper.readValue(message, ObjectNode.class);
        String phoneNo = objectNode.get("PHONE_NO").textValue();
        Integer userId = objectNode.get("USER_ID").intValue();

        Wallet wallet = Wallet.builder()
                .phoneNumber(phoneNo)
                .userId(userId)
                .balance(initialWalletAmount)
                .build();
        walletRepository.save(wallet);
        log.info("Wallet created for user : {}", userId);
    }
}
