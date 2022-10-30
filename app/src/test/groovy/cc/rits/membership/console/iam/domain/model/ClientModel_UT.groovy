package cc.rits.membership.console.iam.domain.model

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.enums.Scope

/**
 * ClientModelの単体テスト
 */
class ClientModel_UT extends AbstractSpecification {

    def "hasScope: スコープを持つかチェック"() {
        given:
        final client = ClientModel.builder()
            .scopes([Scope.USER_READ])
            .build()

        when:
        final result = client.hasScope(scope)

        then:
        result == expectedResult

        where:
        scope           || expectedResult
        Scope.USER_READ || true
        Scope.EMAIL     || false
    }

}
