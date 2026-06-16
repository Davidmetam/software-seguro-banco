package com.sistema_bancario.banco.controller;

import com.sistema_bancario.banco.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MovementController {

    private final TransactionService transactionService;

    @GetMapping("/movements")
    public String list(Authentication authentication, Model model) {
        model.addAttribute("movements", transactionService.getMovements(authentication.getName()));
        return "movements/list";
    }
}
