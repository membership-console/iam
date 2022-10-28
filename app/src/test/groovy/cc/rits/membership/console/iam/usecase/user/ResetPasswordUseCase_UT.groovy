package cc.rits.membership.console.iam.usecase.user

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel
import cc.rits.membership.console.iam.exception.BadRequestException
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.PasswordResetRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * ResetPasswordUseCaseの単体テスト
 */
class ResetPasswordUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    ResetPasswordUseCase sut

    def "handle: パスワードをリセット"() {
        given:
        final passwordResetToken = Spy(PasswordResetTokenModel)
        final requestBody = RandomHelper.mock(PasswordResetRequest)

        final passwordHash = RandomHelper.alphanumeric(10)

        when:
        this.sut.handle(requestBody)

        then:
        noExceptionThrown()
        1 * this.passwordResetTokenRepository.selectByToken(requestBody.token) >> Optional.of(passwordResetToken)
        1 * passwordResetToken.isValid() >> true
        1 * this.authUtil.hashingPassword(requestBody.newPassword) >> passwordHash
        1 * this.userRepository.updatePasswordById(passwordResetToken.userId, passwordHash)
    }

    def "handle: パスワードリセットトークンが期限切れの場合は400エラー"() {
        given:
        final passwordResetToken = Spy(PasswordResetTokenModel)
        final requestBody = RandomHelper.mock(PasswordResetRequest)

        when:
        this.sut.handle(requestBody)

        then:
        1 * this.passwordResetTokenRepository.selectByToken(requestBody.token) >> Optional.of(passwordResetToken)
        1 * passwordResetToken.isValid() >> false
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN))
    }

    def "handle: パスワードリセットトークンが存在しない場合は400エラー"() {
        given:
        final requestBody = RandomHelper.mock(PasswordResetRequest)

        when:
        this.sut.handle(requestBody)

        then:
        1 * this.passwordResetTokenRepository.selectByToken(requestBody.token) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN))
    }

}
