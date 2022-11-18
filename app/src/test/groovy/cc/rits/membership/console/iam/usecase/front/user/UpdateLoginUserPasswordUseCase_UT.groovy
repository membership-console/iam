package cc.rits.membership.console.iam.usecase.front.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.exception.BadRequestException
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.LoginUserPasswordUpdateRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * UpdateLoginUserPasswordUseCaseの単体テスト
 */
class UpdateLoginUserPasswordUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    UpdateLoginUserPasswordUseCase sut

    def "handle: ログインユーザのパスワードを更新"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(LoginUserPasswordUpdateRequest)

        final passwordHash = RandomHelper.alphanumeric(10)

        when:
        this.sut.handle(loginUser, requestBody)

        then:
        noExceptionThrown()
        1 * this.authUtil.isMatchPasswordAndHash(requestBody.oldPassword, loginUser.password) >> true
        1 * this.authUtil.hashingPassword(requestBody.newPassword) >> passwordHash
        1 * this.userRepository.updatePasswordById(loginUser.id, passwordHash)
    }

    def "handle: 現在のパスワードが間違えている場合は400エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(LoginUserPasswordUpdateRequest)

        when:
        this.sut.handle(loginUser, requestBody)

        then:
        1 * this.authUtil.isMatchPasswordAndHash(requestBody.oldPassword, loginUser.password) >> false
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.INVALID_OLD_PASSWORD))
    }

}
