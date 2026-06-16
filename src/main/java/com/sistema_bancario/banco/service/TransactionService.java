package com.sistema_bancario.banco.service;

import com.sistema_bancario.banco.entity.Account;
import com.sistema_bancario.banco.entity.Movement;
import com.sistema_bancario.banco.entity.User;
import com.sistema_bancario.banco.enums.MovementType;
import com.sistema_bancario.banco.exception.InsufficientBalanceException;
import com.sistema_bancario.banco.exception.InvalidTransactionException;
import com.sistema_bancario.banco.repository.AccountRepository;
import com.sistema_bancario.banco.repository.MovementRepository;
import com.sistema_bancario.banco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deposit(String username, BigDecimal amount) {
        BigDecimal normalizedAmount = validatePositive(amount, "El deposito debe ser mayor que cero.");
        Account account = getAccount(username);

        account.setBalance(account.getBalance().add(normalizedAmount));
        movementRepository.save(Movement.builder()
                .type(MovementType.DEPOSIT)
                .amount(normalizedAmount)
                .destinationAccount(account)
                .description("Deposito a cuenta propia")
                .build());
    }

    @Transactional
    public void withdraw(String username, BigDecimal amount) {
        BigDecimal normalizedAmount = validatePositive(amount, "El retiro debe ser mayor que cero.");
        Account account = getAccount(username);
        ensureEnoughBalance(account, normalizedAmount);

        account.setBalance(account.getBalance().subtract(normalizedAmount));
        movementRepository.save(Movement.builder()
                .type(MovementType.WITHDRAW)
                .amount(normalizedAmount)
                .originAccount(account)
                .description("Retiro de cuenta propia")
                .build());
    }

    @Transactional
    public void transfer(String originUsername, String destinationUsername, BigDecimal amount) {
        BigDecimal normalizedAmount = validatePositive(amount, "La transferencia debe ser mayor que cero.");
        String normalizedDestination = destinationUsername.trim();

        if (originUsername.equals(normalizedDestination)) {
            throw new InvalidTransactionException("No puedes transferirte dinero a ti mismo.");
        }

        Account originAccount = getAccount(originUsername);
        User destinationUser = userRepository.findByUsername(normalizedDestination)
                .orElseThrow(() -> new InvalidTransactionException("El usuario destino no existe."));
        Account destinationAccount = accountRepository.findByUser(destinationUser)
                .orElseThrow(() -> new InvalidTransactionException("La cuenta destino no existe."));

        ensureEnoughBalance(originAccount, normalizedAmount);

        originAccount.setBalance(originAccount.getBalance().subtract(normalizedAmount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(normalizedAmount));

        movementRepository.save(Movement.builder()
                .type(MovementType.TRANSFER)
                .amount(normalizedAmount)
                .originAccount(originAccount)
                .destinationAccount(destinationAccount)
                .description("Transferencia a " + normalizedDestination)
                .build());
    }

    @Transactional(readOnly = true)
    public List<Movement> getMovements(String username) {
        Account account = getAccount(username);
        return movementRepository.findByOriginAccountOrDestinationAccountOrderByCreatedAtDesc(account, account);
    }

    private Account getAccount(String username) {
        return accountRepository.findByUserUsername(username)
                .orElseThrow(() -> new InvalidTransactionException("Cuenta no encontrada."));
    }

    private BigDecimal validatePositive(BigDecimal amount, String message) {
        if (amount == null) {
            throw new InvalidTransactionException(message);
        }
        BigDecimal normalized = amount.setScale(2, RoundingMode.HALF_UP);
        if (normalized.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException(message);
        }
        return normalized;
    }

    private void ensureEnoughBalance(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente.");
        }
    }
}
