package com.sistema_bancario.banco.repository;

import com.sistema_bancario.banco.entity.Account;
import com.sistema_bancario.banco.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Account> findByUser(User user);

    @EntityGraph(attributePaths = "user")
    Optional<Account> findByUserUsername(String username);
}
