package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserGroupRepositoryの単体テスト
 */
class UserGroupRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    UserGroupRepositoryImpl sut

    def "selectAll: ユーザグループリストを全件取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "グループA"
            2  | "グループB"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
            1             | Role.PURCHASE_REQUEST_ADMIN.id
            2             | Role.PURCHASE_REQUEST_ADMIN.id
        }
        // @formatter:on

        when:
        final result = this.sut.selectAll()

        then:
        result*.id == [1, 2]
        result*.name == ["グループA", "グループB"]
        result*.roles == [[Role.IAM_ADMIN, Role.PURCHASE_REQUEST_ADMIN], [Role.PURCHASE_REQUEST_ADMIN]]
    }

}
