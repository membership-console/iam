package cc.rits.membership.console.iam.util

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.property.SendGridProperty
import org.spockframework.spring.SpringBean
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Util単体テストの基底クラス
 */
abstract class AbstractUtil_UT extends AbstractSpecification {

    @SpringBean
    PasswordEncoder passwordEncoder = Mock()

    @SpringBean
    SendGridProperty sendGridProperty = Mock()

}
