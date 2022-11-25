package cc.rits.membership.console.iam.usecase.front.client

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
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

    def "handle: クライアントを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        final result = this.sut.handle(loginUser, client.id)

        then:
        1 * this.clientRepository.selectById(client.id) >> Optional.of(client)
        result == client
    }

    def "handle: クライアントが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        this.sut.handle(loginUser, client.id)

        then:
        1 * this.clientRepository.selectById(client.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

}
