package com.sistema_bancario.banco.repository;

import com.sistema_bancario.banco.entity.Account;
import com.sistema_bancario.banco.entity.Movement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    @EntityGraph(attributePaths = {"originAccount", "originAccount.user", "destinationAccount", "destinationAccount.user"})
    List<Movement> findByOriginAccountOrDestinationAccountOrderByCreatedAtDesc(Account originAccount, Account destinationAccount);
}
