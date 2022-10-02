package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserRepositoryの単体テスト
 */
class UserRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    UserRepositoryImpl sut

    def "selectByEmail: メールアドレスからユーザを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email              | password | entrance_year
            1  | "山田"     | "太郎"    | "user@example.com" | ""       | 2000
        }
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "グループA"
            2  | "グループB"
            3  | "グループC"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
            1             | Role.PURCHASE_REQUEST_ADMIN.id
            2             | Role.PURCHASE_REQUEST_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            1       | 1
            1       | 2
        }
        // @formatter:on

        when:
        final result = this.sut.selectByEmail("user@example.com")

        then:
        result.isPresent()
        result.get().firstName == "山田"
        result.get().lastName == "太郎"
        result.get().email == "user@example.com"
        result.get().entranceYear == 2000
        result.get().userGroups*.id == [1, 2]
        result.get().userGroups*.name == ["グループA", "グループB"]
        result.get().userGroups*.roles == [[Role.IAM_ADMIN, Role.PURCHASE_REQUEST_ADMIN], [Role.PURCHASE_REQUEST_ADMIN]]
    }

    def "selectByEmail: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectByEmail("")

        then:
        result.isEmpty()
    }

}
