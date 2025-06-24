package Esprit.tn.EspritJobGetaway.advice;

import Esprit.tn.EspritJobGetaway.Enum.RoleType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class RoleTypeValidator implements ConstraintValidator<ValidRoleType, RoleType> {
    @Override
    public boolean isValid(RoleType value, ConstraintValidatorContext constraintValidatorContext) {
        for (RoleType roleType : RoleType.values()) {
            System.out.println(roleType);
            if (roleType.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
