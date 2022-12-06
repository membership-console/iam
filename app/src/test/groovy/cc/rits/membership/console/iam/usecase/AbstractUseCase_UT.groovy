package cc.rits.membership.console.iam.usecase

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.domain.repository.IClientRepository
import cc.rits.membership.console.iam.domain.repository.IPasswordResetTokenRepository
import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository
import cc.rits.membership.console.iam.domain.repository.IUserRepository
import cc.rits.membership.console.iam.domain.service.ClientService
import cc.rits.membership.console.iam.domain.service.UserGroupService
import cc.rits.membership.console.iam.domain.service.UserService
import cc.rits.membership.console.iam.util.AuthUtil
import cc.rits.membership.console.iam.util.MailUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.spockframework.spring.SpringBean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.web.context.SecurityContextRepository

/**
 * UseCase単体テストの基底クラス
 */
abstract class AbstractUseCase_UT extends AbstractSpecification {

    @SpringBean
    AuthenticationProvider authenticationProvider = Mock()

    @SpringBean
    SecurityContextRepository securityContextRepository = Mock()

    @SpringBean
    HttpSession httpSession = Mock()

    @SpringBean
    HttpServletRequest httpServletRequest = Mock()

    HttpServletResponse httpServletResponse = Mock()

    @SpringBean
    UserService userService = Mock()

    @SpringBean
    UserGroupService userGroupService = Mock()

    @SpringBean
    ClientService clientService = Mock()

    @SpringBean
    IUserRepository userRepository = Mock()

    @SpringBean
    IUserGroupRepository userGroupRepository = Mock()

    @SpringBean
    IPasswordResetTokenRepository passwordResetTokenRepository = Mock()

    @SpringBean
    IClientRepository clientRepository = Mock()

    @SpringBean
    AuthUtil authUtil = Mock()

    @SpringBean
    MailUtil mailUtil = Mock()

}
