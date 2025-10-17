package com.web.rest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true; // La validación de @NotNull/@NotBlank se encarga de esto
        }

        // Regla 1: No puede ser una fecha futura (ya cubierto por @Past pero bueno tenerlo aquí)
        if (birthDate.isAfter(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La fecha de nacimiento no puede ser una fecha futura.")
                   .addConstraintViolation();
            return false;
        }

        // Regla 2: La edad no puede ser mayor a 90 años
        if (Period.between(birthDate, LocalDate.now()).getYears() > 90) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La edad no puede ser mayor a 90 años.")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}