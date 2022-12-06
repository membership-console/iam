package cc.rits.membership.console.iam.infrastructure.api.validation;

import cc.rits.membership.console.iam.infrastructure.api.request.BaseRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequestValidator implements ConstraintValidator<RequestValidated, BaseRequest> {

    @Override
    public void initialize(final RequestValidated constraintAnnotation) {}

    @Override
    public boolean isValid(final BaseRequest baseRequest, final ConstraintValidatorContext constraintValidatorContext) {
        baseRequest.validate();
        return true;
    }

}
