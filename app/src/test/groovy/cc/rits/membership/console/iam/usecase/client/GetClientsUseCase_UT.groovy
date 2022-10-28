package cc.rits.membership.console.iam.usecase.client

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetClientsUseCaseの単体テスト
 */
class GetClientsUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetClientsUseCase sut

    def "handle: IAMの閲覧者がクライアントリストを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final clients = [
            RandomHelper.mock(ClientModel),
            RandomHelper.mock(ClientModel),
        ]

        when:
        final result = this.sut.handle(loginUser)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.clientRepository.selectAll() >> clients
        result == clients
    }

    def "handle: IAMの閲覧者以外はクライアントリストを取得不可"() {
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
