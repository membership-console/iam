package cc.rits.membership.console.iam.usecase.general.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.exception.BadRequestException
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.RequestPasswordResetRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * RequestPasswordResetUseCaseの単体テスト
 */
class RequestPasswordResetUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    RequestPasswordResetUseCase sut

    def "handle: パスワードリセット要求を承認する"() {
        given:
        final user = RandomHelper.mock(UserModel)

        final requestBody = RandomHelper.mock(RequestPasswordResetRequest)
        requestBody.email = user.email

        when:
        this.sut.handle(requestBody)

        then:
        noExceptionThrown()
        1 * this.userRepository.selectByEmail(requestBody.email) >> Optional.of(user)
        1 * this.passwordResetTokenRepository.insert(_)
        1 * this.mailUtil.send([user.email], _, _)
    }

    def "handle: メールアドレスが存在しない場合は400エラー"() {
        given:
        final requestBody = RandomHelper.mock(RequestPasswordResetRequest)

        when:
        this.sut.handle(requestBody)

        then:
        1 * this.userRepository.selectByEmail(requestBody.email) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.REQUESTED_EMAIL_IS_NOT_EXISTS))
    }

}
