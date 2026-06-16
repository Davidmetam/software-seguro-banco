package com.sistema_bancario.banco.controller;

import com.sistema_bancario.banco.dto.RegisterRequest;
import com.sistema_bancario.banco.exception.UserAlreadyExistsException;
import com.sistema_bancario.banco.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(registerRequest);
        } catch (UserAlreadyExistsException exception) {
            bindingResult.rejectValue("username", "username.exists", exception.getMessage());
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Registro completado. Ya puedes iniciar sesion.");
        return "redirect:/login";
    }
}
