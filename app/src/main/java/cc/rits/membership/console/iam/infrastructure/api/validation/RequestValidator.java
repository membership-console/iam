package cc.rits.membership.console.iam.infrastructure.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cc.rits.membership.console.iam.infrastructure.api.request.BaseRequest;

public class RequestValidator implements ConstraintValidator<RequestValidated, BaseRequest> {

    @Override
    public void initialize(RequestValidated constraintAnnotation) {}

    @Override
    public boolean isValid(final BaseRequest baseRequest, final ConstraintValidatorContext constraintValidatorContext) {
        baseRequest.validate();
        return true;
    }

}
