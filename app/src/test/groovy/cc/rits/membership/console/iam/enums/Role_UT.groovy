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
        0  || Optional.of(Role.IAM_VIEWER)
        1  || Optional.of(Role.IAM_ADMIN)
        -1 || Optional.empty()
    }

    def "canImpersonateOtherRole: 別ロールになりすませるかチェック"() {
        when:
        final result = targetRole.canImpersonateOtherRole(impersonatedRole)

        then:
        result == expectedResult

        where:
        targetRole                  | impersonatedRole || expectedResult
        Role.IAM_VIEWER             | Role.IAM_VIEWER  || true
        Role.IAM_ADMIN              | Role.IAM_VIEWER  || true
        Role.IAM_VIEWER             | Role.IAM_ADMIN   || false
        Role.PURCHASE_REQUEST_ADMIN | Role.IAM_VIEWER  || false
    }

}
