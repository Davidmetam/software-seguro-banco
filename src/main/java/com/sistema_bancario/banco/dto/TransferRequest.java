package com.sistema_bancario.banco.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {

    @NotBlank(message = "El usuario destino es obligatorio.")
    @Size(min = 4, max = 50, message = "El usuario destino debe tener entre 4 y 50 caracteres.")
    private String destinationUsername;

    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "0.01", message = "La transferencia debe ser mayor que cero.")
    @Digits(integer = 13, fraction = 2, message = "El monto debe tener maximo 13 enteros y 2 decimales.")
    private BigDecimal amount;
}
