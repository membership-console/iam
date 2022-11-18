package cc.rits.membership.console.iam.usecase.front.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUserUseCaseの単体テスト
 */
class GetUserUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUserUseCase sut

    def "handle: IAMの閲覧者がユーザを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        when:
        final result = this.sut.handle(loginUser, user.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.of(user)
        result == user
    }

    def "handle: IAMの閲覧者以外はユーザを取得不可"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        when:
        this.sut.handle(loginUser, user.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        when:
        this.sut.handle(loginUser, user.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

}
