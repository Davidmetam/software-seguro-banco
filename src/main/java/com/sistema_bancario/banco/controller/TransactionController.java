package com.sistema_bancario.banco.controller;

import com.sistema_bancario.banco.dto.DepositRequest;
import com.sistema_bancario.banco.dto.TransferRequest;
import com.sistema_bancario.banco.dto.WithdrawRequest;
import com.sistema_bancario.banco.exception.InsufficientBalanceException;
import com.sistema_bancario.banco.exception.InvalidTransactionException;
import com.sistema_bancario.banco.service.AccountService;
import com.sistema_bancario.banco.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @GetMapping("/deposit")
    public String showDeposit(Model model, Authentication authentication) {
        if (!model.containsAttribute("depositRequest")) {
            model.addAttribute("depositRequest", new DepositRequest());
        }
        model.addAttribute("account", accountService.getByUsername(authentication.getName()));
        return "transactions/deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@Valid @ModelAttribute DepositRequest depositRequest,
                          BindingResult bindingResult,
                          Authentication authentication,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/deposit";
        }

        try {
            transactionService.deposit(authentication.getName(), depositRequest.getAmount());
        } catch (InvalidTransactionException exception) {
            bindingResult.rejectValue("amount", "deposit.invalid", exception.getMessage());
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/deposit";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Deposito realizado correctamente.");
        return "redirect:/dashboard";
    }

    @GetMapping("/withdraw")
    public String showWithdraw(Model model, Authentication authentication) {
        if (!model.containsAttribute("withdrawRequest")) {
            model.addAttribute("withdrawRequest", new WithdrawRequest());
        }
        model.addAttribute("account", accountService.getByUsername(authentication.getName()));
        return "transactions/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@Valid @ModelAttribute WithdrawRequest withdrawRequest,
                           BindingResult bindingResult,
                           Authentication authentication,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/withdraw";
        }

        try {
            transactionService.withdraw(authentication.getName(), withdrawRequest.getAmount());
        } catch (InvalidTransactionException | InsufficientBalanceException exception) {
            bindingResult.rejectValue("amount", "withdraw.invalid", exception.getMessage());
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/withdraw";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Retiro realizado correctamente.");
        return "redirect:/dashboard";
    }

    @GetMapping("/transfer")
    public String showTransfer(Model model, Authentication authentication) {
        if (!model.containsAttribute("transferRequest")) {
            model.addAttribute("transferRequest", new TransferRequest());
        }
        model.addAttribute("account", accountService.getByUsername(authentication.getName()));
        return "transactions/transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@Valid @ModelAttribute TransferRequest transferRequest,
                           BindingResult bindingResult,
                           Authentication authentication,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/transfer";
        }

        try {
            transactionService.transfer(authentication.getName(), transferRequest.getDestinationUsername(), transferRequest.getAmount());
        } catch (InsufficientBalanceException exception) {
            bindingResult.rejectValue("amount", "transfer.balance", exception.getMessage());
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/transfer";
        } catch (InvalidTransactionException exception) {
            bindingResult.reject("transfer.invalid", exception.getMessage());
            model.addAttribute("account", accountService.getByUsername(authentication.getName()));
            return "transactions/transfer";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Transferencia realizada correctamente.");
        return "redirect:/dashboard";
    }
}
