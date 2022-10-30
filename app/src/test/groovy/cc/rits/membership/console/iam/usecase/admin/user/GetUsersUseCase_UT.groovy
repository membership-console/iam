package cc.rits.membership.console.iam.usecase.admin.user

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Scope
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

    def "handle: ユーザリストを取得"() {
        given:
        final loginClient = Spy(ClientModel)
        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        final result = this.sut.handle(loginClient)

        then:
        1 * loginClient.hasScope(Scope.USER_READ) >> true
        1 * this.userRepository.selectAll() >> users
        result == users
    }

    def "handle: 対象スコープを持たない場合はユーザリストを取得不可"() {
        given:
        final loginClient = Spy(ClientModel)

        when:
        this.sut.handle(loginClient)

        then:
        1 * loginClient.hasScope(Scope.USER_READ) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION))
    }

}
