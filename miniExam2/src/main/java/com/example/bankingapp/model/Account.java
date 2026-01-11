package com.example.bankingapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    public enum AccountType {
        CHECKING, DEPOSIT
    }

    public enum Currency {
        EUR, GBP, USD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 22)
    @Pattern(regexp = "^BG\\d{2}[A-Z]{4}\\d{4}[A-Z0-9]{10}$", 
             message = "Invalid IBAN format. Expected format: BG80BNBG96611020345678")
    private String iban;

    @Column(nullable = false, length = 50)
    @Size(min = 6, max = 50, message = "Account holder name must be between 6 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Account holder name must contain only letters and spaces")
    private String accountHolder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false, precision = 19, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be positive")
    private BigDecimal balance;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
