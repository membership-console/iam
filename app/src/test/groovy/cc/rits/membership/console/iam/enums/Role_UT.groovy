package cc.rits.membership.console.iam.enums

import cc.rits.membership.console.iam.AbstractSpecification

/**
 * Roleの単体テスト
 */
class Role_UT extends AbstractSpecification {

    def "find: ロールを検索"() {
        when:
        final result = Role.find(id)

        then:
        result == expectedResult

        where:
        id || expectedResult
        0  || Optional.of(Role.IAM_ADMIN)
        1  || Optional.of(Role.PURCHASE_REQUEST_ADMIN)
        -1 || Optional.empty()
    }

}
