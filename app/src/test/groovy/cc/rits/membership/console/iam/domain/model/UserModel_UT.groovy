package cc.rits.membership.console.iam.domain.model

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.enums.Role

/**
 * UserModelの単体テスト
 */
class UserModel_UT extends AbstractSpecification {

    def "hasRole: ロールを持つかチェック"() {
        given:
        final userGroup = UserGroupModel.builder()
            .role(Role.IAM_ADMIN)
            .build()
        final user = UserModel.builder()
            .userGroup(userGroup)
            .build()

        when:
        final result = user.hasRole(role)

        then:
        result == expectedResult

        where:
        role                 || expectedResult
        Role.IAM_ADMIN       || true
        Role.PAYMASTER_ADMIN || false
    }

}
