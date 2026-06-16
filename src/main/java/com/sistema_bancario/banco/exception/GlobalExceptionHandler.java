package com.sistema_bancario.banco.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception exception, Model model) {
        model.addAttribute("errorMessage", "Ocurrio un error inesperado. Intenta nuevamente.");
        return "error/error";
    }
}
