package cc.rits.membership.console.iam.usecase.client

import cc.rits.membership.console.iam.domain.model.ClientModel
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
 * GetClientUseCaseの単体テスト
 */
class GetClientUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetClientUseCase sut

    def "handle: IAMの閲覧者がクライアントを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        final result = this.sut.handle(loginUser, client.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.of(client)
        result == client
    }

    def "handle: IAMの閲覧者以外はクライアントを取得不可"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        final result = this.sut.handle(loginUser, client.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: クライアントが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        final result = this.sut.handle(loginUser, client.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

}
