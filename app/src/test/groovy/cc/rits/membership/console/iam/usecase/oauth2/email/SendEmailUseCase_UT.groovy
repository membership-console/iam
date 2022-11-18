package cc.rits.membership.console.iam.usecase.oauth2.email

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.EmailSendRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * SendEmailUseCaseの単体テスト
 */
class SendEmailUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    SendEmailUseCase sut

    def "handle: メールを送信"() {
        given:
        final loginClient = Spy(ClientModel)
        final requestBody = RandomHelper.mock(EmailSendRequest)

        when:
        this.sut.handle(loginClient, requestBody)

        then:
        1 * loginClient.hasScope(Scope.EMAIL) >> true
        1 * this.userRepository.selectEmailsByIds(requestBody.userIds) >> []
        1 * this.mailUtil.send(_, _, _)
    }

    def "handle: 対象スコープを持たない場合はメールを送信不可"() {
        given:
        final loginClient = Spy(ClientModel)
        final requestBody = RandomHelper.mock(EmailSendRequest)

        when:
        this.sut.handle(loginClient, requestBody)

        then:
        1 * loginClient.hasScope(Scope.EMAIL) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION))
    }

}
