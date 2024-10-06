package org.deepaksharma.transactionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitiateTransactionRequest {

    @NotBlank
    String receiverPhoneNo;

    @Positive
    Double amount;

    String description;
}
