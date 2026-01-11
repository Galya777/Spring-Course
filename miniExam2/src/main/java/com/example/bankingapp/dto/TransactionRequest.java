package com.example.bankingapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotBlank(message = "Source account IBAN is required")
    @Pattern(regexp = "^BG\\d{2}[A-Z]{4}\\d{4}[A-Z0-9]{10}$", 
             message = "Invalid source IBAN format")
    private String fromIban;

    @NotBlank(message = "Destination account IBAN is required")
    @Pattern(regexp = "^BG\\d{2}[A-Z]{4}\\d{4}[A-Z0-9]{10}$", 
             message = "Invalid destination IBAN format")
    private String toIban;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Client name is required")
    @Size(min = 6, max = 50, message = "Client name must be between 6 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Client name must contain only letters and spaces")
    private String clientName;
}
