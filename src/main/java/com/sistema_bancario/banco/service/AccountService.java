package com.sistema_bancario.banco.service;

import com.sistema_bancario.banco.entity.Account;
import com.sistema_bancario.banco.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account getByUsername(String username) {
        return accountRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada."));
    }
}
