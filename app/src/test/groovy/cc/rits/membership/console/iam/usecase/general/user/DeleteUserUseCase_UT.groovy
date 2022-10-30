package cc.rits.membership.console.iam.usecase.general.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * DeleteUserUseCaseの単体テスト
 */
class DeleteUserUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DeleteUserUseCase sut

    def "handle: IAMの管理者がユーザを削除"() {
        given:
        final loginUser = Spy(UserModel)
        final userId = 1

        when:
        this.sut.handle(loginUser, userId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userRepository.existsById(userId) >> true
        1 * this.userRepository.deleteById(userId)
        noExceptionThrown()
    }

    def "handle: IAMの管理者以外はユーザを削除不可"() {
        given:
        final loginUser = Spy(UserModel)
        final userId = 1

        when:
        this.sut.handle(loginUser, userId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final userId = 1

        when:
        this.sut.handle(loginUser, userId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userRepository.existsById(userId) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

}
