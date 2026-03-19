package com.adhiraj.agenticdev.accountservice.repository;

import com.adhiraj.agenticdev.accountservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
