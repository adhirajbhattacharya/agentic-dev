package com.adhiraj.agenticdev.accountservice.service;

import com.adhiraj.agenticdev.accountservice.domain.Account;
import com.adhiraj.agenticdev.accountservice.domain.AccountStatus;
import com.adhiraj.agenticdev.accountservice.repository.AccountRepository;
import com.adhiraj.agenticdev.accountservice.service.AccountService.CreateResult;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock AccountRepository accountRepository;
    @InjectMocks AccountService accountService;

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private Account stubAccount(UUID id, String email) {
        Account account = new Account();
        account.setId(id);
        account.setEmail(email);
        account.setStatus(AccountStatus.ACTIVE);
        return account;
    }

    @Test
    void create_newEmail_savesAndReturnsCreated() {
        when(accountRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        Account saved = stubAccount(ID, "new@example.com");
        when(accountRepository.save(any())).thenReturn(saved);

        CreateResult result = accountService.create("new@example.com");

        assertThat(result.created()).isTrue();
        assertThat(result.account()).isSameAs(saved);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void create_existingEmail_returnsExistingWithoutSave() {
        Account existing = stubAccount(ID, "existing@example.com");
        when(accountRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existing));

        CreateResult result = accountService.create("existing@example.com");

        assertThat(result.created()).isFalse();
        assertThat(result.account()).isSameAs(existing);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void findAll_returnsAllActiveAccounts() {
        List<Account> accounts = List.of(stubAccount(ID, "a@example.com"));
        when(accountRepository.findAll()).thenReturn(accounts);

        assertThat(accountService.findAll()).isEqualTo(accounts);
    }

    @Test
    void softDelete_existingId_setsStatusToDeleted() {
        Account account = stubAccount(ID, "a@example.com");
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));

        accountService.softDelete(ID);

        assertThat(account.getStatus()).isEqualTo(AccountStatus.DELETED);
    }

    @Test
    void softDelete_unknownId_throwsEntityNotFoundException() {
        when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.softDelete(ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(ID.toString());
    }
}
