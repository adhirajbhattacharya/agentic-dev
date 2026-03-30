package com.adhiraj.agenticdev.accountservice.web;

import com.adhiraj.agenticdev.accountservice.domain.Account;
import com.adhiraj.agenticdev.accountservice.domain.AccountStatus;
import com.adhiraj.agenticdev.accountservice.service.AccountService;
import com.adhiraj.agenticdev.accountservice.service.AccountService.CreateResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean AccountService accountService;

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private Account stubAccount(UUID id, String email) {
        Account account = new Account();
        account.setId(id);
        account.setEmail(email);
        account.setStatus(AccountStatus.ACTIVE);
        return account;
    }

    @Test
    void post_newEmail_returns201WithAccount() throws Exception {
        Account account = stubAccount(ID, "test@example.com");
        when(accountService.create("test@example.com")).thenReturn(new CreateResult(account, true));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void post_existingEmail_returns200WithExistingAccount() throws Exception {
        Account account = stubAccount(ID, "test@example.com");
        when(accountService.create("test@example.com")).thenReturn(new CreateResult(account, false));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void get_returnsAccountList() throws Exception {
        when(accountService.findAll()).thenReturn(List.of(stubAccount(ID, "test@example.com")));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID.toString()))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void get_emptyList_returns200WithEmptyArray() throws Exception {
        when(accountService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        mockMvc.perform(delete("/accounts/{id}", ID))
                .andExpect(status().isNoContent());
        verify(accountService).softDelete(ID);
    }

    @Test
    void delete_unknownId_returns404WithErrorMessage() throws Exception {
        doThrow(new EntityNotFoundException("Account not found: " + ID))
                .when(accountService).softDelete(ID);

        mockMvc.perform(delete("/accounts/{id}", ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account not found: " + ID));
    }
}
