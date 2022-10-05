package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.domain.model.UserGroupModel
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

    def "selectById: IDからユーザグループを取得"() {
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
        final result = this.sut.selectById(1)

        then:
        result.isPresent()
        result.get().id == 1
        result.get().name == "グループA"
        result.get().roles == [Role.IAM_ADMIN, Role.PURCHASE_REQUEST_ADMIN]
    }

    def "selectById: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectById(1)

        then:
        result.isEmpty()
    }

    def "insert: ユーザグループを作成"() {
        given:
        final userGroup = UserGroupModel.builder()
            .name("")
            .roles([Role.IAM_VIEWER, Role.IAM_ADMIN])
            .build()

        when:
        sut.insert(userGroup)

        then:
        final createdUserGroup = sql.firstRow("SELECT * FROM user_group")
        createdUserGroup.name == userGroup.name

        final createdUserGroupRoles = sql.rows("SELECT * FROM user_group_role")
        createdUserGroupRoles*.user_group_id == [createdUserGroup.id, createdUserGroup.id]
        createdUserGroupRoles*.role_id == [Role.IAM_VIEWER.id, Role.IAM_ADMIN.id]
    }

    def "existsByName: ユーザグループ名の存在確認"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "A"
        }
        // @formatter:on

        when:
        final result = this.sut.existsByName(inputName)

        then:
        result == expectedResult

        where:
        inputName || expectedResult
        "A"       || true
        "B"       || false
    }

    def "existsById: IDからユーザグループの存在確認"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
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

    def "deleteById: IDからユーザグループを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
        }
        // @formatter:on

        when:
        this.sut.deleteById(1)

        then:
        final userGroups = sql.rows("SELECT * FROM user_group")
        userGroups == []
    }

}
