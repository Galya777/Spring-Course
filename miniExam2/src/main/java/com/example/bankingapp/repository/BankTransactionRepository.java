package com.example.bankingapp.repository;

import com.example.bankingapp.model.Account;
import com.example.bankingapp.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
    @Query("SELECT t FROM BankTransaction t WHERE t.fromAccount.iban = :accountIban OR t.toAccount.iban = :accountIban " +
           "ORDER BY t.transactionDate DESC LIMIT 10")
    List<BankTransaction> findLast10TransactionsByAccountIban(@Param("accountIban") String accountIban);
    
    @Query("SELECT t FROM BankTransaction t WHERE t.fromAccount = :account OR t.toAccount = :account " +
           "ORDER BY t.transactionDate DESC")
    List<BankTransaction> findAllTransactionsByAccount(@Param("account") Account account);
}
