package org.deepaksharma.transactionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deepaksharma.transactionservice.client.UserServiceClient;
import org.deepaksharma.transactionservice.dto.InitiateTransactionRequest;
import org.deepaksharma.transactionservice.enums.TransactionStatus;
import org.deepaksharma.transactionservice.model.Transaction;
import org.deepaksharma.transactionservice.repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.deepaksharma.transactionservice.constants.KafkaConstants.TRANSACTION_INITIATED_TOPIC;
import static org.deepaksharma.transactionservice.constants.TransactionInitiatedConstants.SENDER_PHONE_NO;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    private final TransactionRepository transactionRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;


    @Override
    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
        String auth = "transaction-service:transaction-service";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        log.info("Auth header: {}", authHeader);

        ObjectNode node = userServiceClient.getUser(phoneNo, authHeader);
        if (node == null) {
            throw new UsernameNotFoundException("User not found");
        }
        log.info("Current User details: {}", node);

        ArrayNode authorities = (ArrayNode) node.get("authorities");
        log.info("Authorities: {}", authorities);

        final List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
        authorities.iterator().forEachRemaining(jsonNode -> grantedAuthoritiesList.add(new SimpleGrantedAuthority(jsonNode.get("authority").textValue())));

        return new User(node.get("phoneNo").textValue(), node.get("password").textValue(), grantedAuthoritiesList);
    }

    public String initiateTransaction(String senderPhoneNo, InitiateTransactionRequest request) {
        log.info("Initiating transaction for: {}", request);
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .senderPhoneNo(senderPhoneNo)
                .receiverPhoneNo(request.getReceiverPhoneNo())
                .amount(request.getAmount())
                .status(TransactionStatus.INITIATED)
                .description(request.getDescription() != null ? request.getDescription() : "")
                .build();

        transactionRepository.save(transaction);
        log.info("Transaction saved: {}", transaction);
        log.info("Sending transaction initiated event to Kafka");

        // Requirement for sending kafka message
        // Sender Phone No, Receiver Phone No, Amount, Transaction_Id

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(SENDER_PHONE_NO, transaction.getSenderPhoneNo());
        objectNode.put("RECEIVER_PHONE_NO", request.getReceiverPhoneNo());
        objectNode.put("AMOUNT", request.getAmount());
        objectNode.put("TRANSACTION_ID", transaction.getTransactionId());

        String kafkaMessage = objectNode.toString();
        kafkaTemplate.send(TRANSACTION_INITIATED_TOPIC, kafkaMessage);
        log.info("Transaction initiated event sent to Kafka");


        return transaction.getTransactionId();
    }
}
