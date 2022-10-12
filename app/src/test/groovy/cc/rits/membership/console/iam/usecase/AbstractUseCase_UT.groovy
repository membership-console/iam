package cc.rits.membership.console.iam.usecase

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.config.auth.IamAuthenticationProvider
import cc.rits.membership.console.iam.domain.repository.PasswordResetTokenRepository
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository
import cc.rits.membership.console.iam.domain.repository.UserRepository
import cc.rits.membership.console.iam.domain.service.UserGroupService
import cc.rits.membership.console.iam.domain.service.UserService
import cc.rits.membership.console.iam.util.AuthUtil
import cc.rits.membership.console.iam.util.MailUtil
import org.spockframework.spring.SpringBean

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

/**
 * UseCase単体テストの基底クラス
 */
abstract class AbstractUseCase_UT extends AbstractSpecification {

    @SpringBean
    IamAuthenticationProvider authenticationProvider = Mock()

    @SpringBean
    HttpSession httpSession = Mock()

    @SpringBean
    HttpServletRequest httpServletRequest = Mock()

    @SpringBean
    UserService userService = Mock()

    @SpringBean
    UserGroupService userGroupService = Mock()

    @SpringBean
    UserRepository userRepository = Mock()

    @SpringBean
    UserGroupRepository userGroupRepository = Mock()

    @SpringBean
    PasswordResetTokenRepository passwordResetTokenRepository = Mock()

    @SpringBean
    AuthUtil authUtil = Mock()

    @SpringBean
    MailUtil mailUtil = Mock()

}
