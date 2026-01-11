package com.example.bankingapp.dto;

import com.example.bankingapp.model.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {
    @NotBlank(message = "IBAN is required")
    @Pattern(regexp = "^BG\\d{2}[A-Z]{4}\\d{4}[A-Z0-9]{10}$", 
             message = "Invalid IBAN format. Expected format: BG80BNBG96611020345678")
    private String iban;

    @NotBlank(message = "Account holder name is required")
    @Size(min = 6, max = 50, message = "Account holder name must be between 6 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Account holder name must contain only letters and spaces")
    private String accountHolder;

    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;

    @NotNull(message = "Currency is required")
    private Account.Currency currency;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial balance must be positive")
    private BigDecimal balance;
}
