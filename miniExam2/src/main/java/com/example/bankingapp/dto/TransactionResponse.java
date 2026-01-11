package com.example.bankingapp.dto;

import com.example.bankingapp.model.Account;
import com.example.bankingapp.model.BankTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private Account.Currency currency;
    private String clientName;
    private LocalDateTime transactionDate;

    public static TransactionResponse fromEntity(BankTransaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .fromIban(transaction.getFromAccount().getIban())
                .toIban(transaction.getToAccount().getIban())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .clientName(transaction.getClientName())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
