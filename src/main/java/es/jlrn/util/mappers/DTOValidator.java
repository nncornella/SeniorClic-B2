package es.jlrn.util.mappers;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class DTOValidator {
//
    private final Validator validator;

    public DTOValidator(Validator validator) {
        this.validator = validator;
    }

    public boolean isValid(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        return violations.isEmpty();
    }
}
