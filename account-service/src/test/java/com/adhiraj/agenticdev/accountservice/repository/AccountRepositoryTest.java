package com.adhiraj.agenticdev.accountservice.repository;

import com.adhiraj.agenticdev.accountservice.domain.Account;
import com.adhiraj.agenticdev.accountservice.domain.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findAll_shouldExcludeSoftDeletedAccounts() {
        // Arrange — persist two accounts directly via EntityManager
        Account active = new Account();
        active.setEmail("active@example.com");
        entityManager.persistAndFlush(active);

        Account deleted = new Account();
        deleted.setEmail("deleted@example.com");
        deleted.setStatus(AccountStatus.DELETED);
        entityManager.persistAndFlush(deleted);

        // Act
        List<Account> result = accountRepository.findAll();

        // Assert — only the active account is returned
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("active@example.com");
    }
}
