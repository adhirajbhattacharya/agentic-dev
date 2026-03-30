package com.adhiraj.agenticdev.accountservice.web;

import com.adhiraj.agenticdev.accountservice.service.AccountService;
import com.adhiraj.agenticdev.accountservice.service.AccountService.CreateResult;
import com.adhiraj.agenticdev.accountservice.web.dto.AccountResponse;
import com.adhiraj.agenticdev.accountservice.web.dto.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
        CreateResult result = accountService.create(request.email());
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(AccountResponse.from(result.account()));
    }

    @GetMapping
    public List<AccountResponse> listAll() {
        return accountService.findAll().stream()
                .map(AccountResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        accountService.softDelete(id);
    }
}
