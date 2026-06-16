package com.sistema_bancario.banco.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequest {

    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "0.01", message = "El deposito debe ser mayor que cero.")
    @Digits(integer = 13, fraction = 2, message = "El monto debe tener maximo 13 enteros y 2 decimales.")
    private BigDecimal amount;
}
