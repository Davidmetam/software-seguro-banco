package com.sistema_bancario.banco.service;

import com.sistema_bancario.banco.dto.RegisterRequest;
import com.sistema_bancario.banco.entity.Account;
import com.sistema_bancario.banco.entity.User;
import com.sistema_bancario.banco.exception.UserAlreadyExistsException;
import com.sistema_bancario.banco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest request) {
        String normalizedUsername = request.getUsername().trim();

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new UserAlreadyExistsException("El usuario ya existe.");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .username(normalizedUsername)
                .pinHash(passwordEncoder.encode(request.getPin()))
                .role("ROLE_USER")
                .enabled(true)
                .build();

        Account account = Account.builder()
                .user(user)
                .balance(BigDecimal.ZERO.setScale(2))
                .build();

        user.setAccount(account);
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

}
