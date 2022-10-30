package cc.rits.membership.console.iam.usecase.general.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUsersUseCaseの単体テスト
 */
class GetUsersUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUsersUseCase sut

    def "handle: IAMの閲覧者がユーザリストを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        final result = this.sut.handle(loginUser)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.userRepository.selectAll() >> users
        result == users
    }

    def "handle: IAMの閲覧者以外はユーザリストを取得不可"() {
        given:
        final loginUser = Spy(UserModel)

        when:
        this.sut.handle(loginUser)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

}
