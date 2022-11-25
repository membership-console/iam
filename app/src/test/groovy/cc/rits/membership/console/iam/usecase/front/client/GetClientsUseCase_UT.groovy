package cc.rits.membership.console.iam.usecase.front.client

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetClientsUseCaseの単体テスト
 */
class GetClientsUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetClientsUseCase sut

    def "handle: クライアントリストを取得"() {
        given:
        final clients = [
            RandomHelper.mock(ClientModel),
            RandomHelper.mock(ClientModel),
        ]

        when:
        final result = this.sut.handle()

        then:
        1 * this.clientRepository.selectAll() >> clients
        result == clients
    }

}
