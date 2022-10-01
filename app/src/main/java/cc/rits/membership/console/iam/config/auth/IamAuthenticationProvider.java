package cc.rits.membership.console.iam.config.auth;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
 * IAM Authentication Provider
 */
public class IamAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void doAfterPropertiesSet() {}

}
