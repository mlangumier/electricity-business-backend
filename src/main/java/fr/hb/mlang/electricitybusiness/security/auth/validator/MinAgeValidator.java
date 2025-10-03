package fr.hb.mlang.electricitybusiness.security.auth.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

  private int minAge;

  @Override
  public void initialize(MinAge constraintAnnotation) {
    this.minAge = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
    if (dateOfBirth == null) {
      return true;
    }
    return dateOfBirth.plusYears(minAge).isBefore(LocalDate.now()) || dateOfBirth
        .plusYears(minAge)
        .isEqual(LocalDate.now());
  }
}
