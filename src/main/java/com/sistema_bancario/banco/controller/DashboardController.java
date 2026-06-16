package com.sistema_bancario.banco.controller;

import com.sistema_bancario.banco.entity.Account;
import com.sistema_bancario.banco.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AccountService accountService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication authentication, Model model) {
        Account account = accountService.getByUsername(authentication.getName());
        model.addAttribute("user", account.getUser());
        model.addAttribute("account", account);
        return "dashboard/index";
    }
}
