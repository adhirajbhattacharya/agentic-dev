package com.adhiraj.agenticdev.accountservice.service;

import com.adhiraj.agenticdev.accountservice.domain.Account;
import com.adhiraj.agenticdev.accountservice.domain.AccountStatus;
import com.adhiraj.agenticdev.accountservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    public record CreateResult(Account account, boolean created) {}

    private final AccountRepository accountRepository;

    public CreateResult create(String email) {
        return accountRepository.findByEmail(email)
                .map(a -> new CreateResult(a, false))
                .orElseGet(() -> {
                    Account account = new Account();
                    account.setEmail(email);
                    return new CreateResult(accountRepository.save(account), true);
                });
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public void softDelete(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + id));
        account.setStatus(AccountStatus.DELETED);
    }
}
