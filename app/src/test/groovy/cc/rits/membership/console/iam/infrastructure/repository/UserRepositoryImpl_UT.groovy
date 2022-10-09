package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.domain.model.UserGroupModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.helper.RandomHelper
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
        result.get().id == 1
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

    def "selectById: IDからユーザを取得"() {
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
        final result = this.sut.selectById(1)

        then:
        result.isPresent()
        result.get().id == 1
        result.get().firstName == "山田"
        result.get().lastName == "太郎"
        result.get().email == "user@example.com"
        result.get().entranceYear == 2000
        result.get().userGroups*.id == [1, 2]
        result.get().userGroups*.name == ["グループA", "グループB"]
        result.get().userGroups*.roles == [[Role.IAM_ADMIN, Role.PURCHASE_REQUEST_ADMIN], [Role.PURCHASE_REQUEST_ADMIN]]
    }

    def "selectById: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectById(1)

        then:
        result.isEmpty()
    }

    def "selectAll: ユーザリストを全件取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            1  | ""         | ""        | "user1@example.com" | ""       | 2000
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
        }
        // @formatter:on

        when:
        final result = this.sut.selectAll()

        then:
        result*.id == [1, 2]
        result*.email == ["user1@example.com", "user2@example.com"]
    }

    def "insert: ユーザを作成"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "1"
            2  | "2"
            3  | "3"
        }
        // @formatter:on

        final user = UserModel.builder()
            .firstName(RandomHelper.alphanumeric(10))
            .lastName(RandomHelper.alphanumeric(10))
            .email(RandomHelper.email())
            .password(RandomHelper.alphanumeric(10))
            .entranceYear(2000)
            .userGroup(UserGroupModel.builder().id(1).build())
            .userGroup(UserGroupModel.builder().id(2).build())
            .build()

        when:
        this.sut.insert(user)

        then:
        final createdUser = sql.firstRow("SELECT * FROM user")
        createdUser.first_name == user.firstName
        createdUser.last_name == user.lastName
        createdUser.email == user.email
        createdUser.password == user.password
        createdUser.entrance_year == user.entranceYear

        final created_r__user__user_group_list = sql.rows("SELECT * FROM r__user__user_group")
        created_r__user__user_group_list*.user_id == user.userGroups.collect { createdUser.id }
        created_r__user__user_group_list*.user_group_id == user.userGroups*.id
    }


    def "deleteById: IDからユーザを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        // @formatter:on

        when:
        this.sut.deleteById(1)

        then:
        final users = sql.rows("SELECT * FROM user")
        users == []
    }

    def "existsById: IDからユーザの存在確認"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        // @formatter:on

        when:
        final result = this.sut.existsById(inputId)

        then:
        result == expectedResult

        where:
        inputId || expectedResult
        1       || true
        2       || false
    }

    def "existsByEmail: メールアドレスからユーザの存在確認"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email           | password | entrance_year
            1  | ""         | ""        | "1@example.com" | ""       | 2000
        }
        // @formatter:on

        when:
        final result = this.sut.existsByEmail(inputEmail)

        then:
        result == expectedResult

        where:
        inputEmail      || expectedResult
        "1@example.com" || true
        "2@example.com" || false
    }

    def "countByUserGroupId: ユーザグループIDからユーザ数を取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            1  | ""         | ""        | "user1@example.com" | ""       | 2000
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
            3  | ""         | ""        | "user3@example.com" | ""       | 2000
        }
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "A"
            2  | "B"
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            1       | 1
            2       | 1
            3       | 2
        }
        // @formatter:on

        when:
        final result = this.sut.countByUserGroupId(inputId)

        then:
        result == expectedResult

        where:
        inputId | expectedResult
        1       | 2
        2       | 1
    }

    def "updatePasswordById: IDからパスワードを更新"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email              | password | entrance_year
            1  | "山田"     | "太郎"    | "user@example.com" | ""       | 2000
        }
        // @formatter:on

        final newPassword = RandomHelper.alphanumeric(10)

        when:
        this.sut.updatePasswordById(1, newPassword)

        then:
        final updatedUser = sql.firstRow("SELECT password FROM user")
        updatedUser.password == newPassword
    }

}
