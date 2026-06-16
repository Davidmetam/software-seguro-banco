package com.sistema_bancario.banco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres.")
    private String name;

    @NotBlank(message = "El usuario es obligatorio.")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El usuario solo puede contener letras, numeros, puntos, guiones y guiones bajos.")
    private String username;

    @NotBlank(message = "El PIN es obligatorio.")
    @Size(min = 4, max = 8, message = "El PIN debe tener entre 4 y 8 digitos.")
    @Pattern(regexp = "^\\d+$", message = "El PIN solo debe contener numeros.")
    private String pin;
}
