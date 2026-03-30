package com.adhiraj.agenticdev.accountservice.web.dto;

import com.adhiraj.agenticdev.accountservice.domain.Account;
import java.util.UUID;

public record AccountResponse(UUID id, String email, String status) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getId(), account.getEmail(), account.getStatus().name());
    }
}
