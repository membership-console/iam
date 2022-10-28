package cc.rits.membership.console.iam.enums

import cc.rits.membership.console.iam.AbstractSpecification

/**
 * Scopeの単体テスト
 */
class Scope_UT extends AbstractSpecification {

    def "find: スコープを検索"() {
        when:
        final result = Scope.find(name)

        then:
        result == expectedResult

        where:
        name                      || expectedResult
        Scope.USER_READ.getName() || Optional.of(Scope.USER_READ)
        Scope.EMAIL.getName()     || Optional.of(Scope.EMAIL)
        ""                        || Optional.empty()
    }

}
