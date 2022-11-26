package cc.rits.membership.console.iam.domain.service

import cc.rits.membership.console.iam.helper.RandomHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * ClientServiceの単体テスト
 */
class ClientService_UT extends AbstractService_UT {

    @Autowired
    ClientService sut

    def "checkIsNameAlreadyUsed: クライアント名が使われていない場合は何もしない"() {
        given:
        final name = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsNameAlreadyUsed(name)

        then:
        1 * this.clientRepository.existsByName(name) >> false
        noExceptionThrown()
    }

}
