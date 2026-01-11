package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountRequest;
import com.example.bankingapp.dto.AccountResponse;
import com.example.bankingapp.exception.AccountNotFoundException;
import com.example.bankingapp.exception.DuplicateAccountException;
import com.example.bankingapp.model.Account;
import com.example.bankingapp.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        if (accountRepository.existsByIban(request.getIban())) {
            throw new DuplicateAccountException("Account with IBAN " + request.getIban() + " already exists");
        }

        Account account = new Account();
        account.setIban(request.getIban());
        account.setAccountHolder(request.getAccountHolder());
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getBalance());

        Account savedAccount = accountRepository.save(account);
        return AccountResponse.fromEntity(savedAccount);
    }

    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

        if (!account.getIban().equals(request.getIban()) && 
            accountRepository.existsByIban(request.getIban())) {
            throw new DuplicateAccountException("IBAN " + request.getIban() + " is already in use");
        }

        account.setIban(request.getIban());
        account.setAccountHolder(request.getAccountHolder());
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getBalance());

        Account updatedAccount = accountRepository.save(account);
        return AccountResponse.fromEntity(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        accountRepository.delete(account);
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return AccountResponse.fromEntity(account);
    }

    public AccountResponse getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));
        return AccountResponse.fromEntity(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalBankBalance() {
        return accountRepository.findAll().stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
