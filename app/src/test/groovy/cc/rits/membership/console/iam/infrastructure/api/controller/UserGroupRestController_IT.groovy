package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.*
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.request.UserGroupUpsertRequest
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupResponse
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupsResponse
import org.springframework.http.HttpStatus
import spock.lang.Shared

/**
 * UserGroupRestControllerの統合テスト
 */
class UserGroupRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/user-groups"
    static final String GET_USER_GROUPS_PATH = BASE_PATH
    static final String GET_USER_GROUP_PATH = BASE_PATH + "/%d"
    static final String CREATE_USER_GROUP_PATH = BASE_PATH
    static final String DELETE_USER_GROUP_PATH = BASE_PATH + "/%d"

    @Shared
    UserGroupUpsertRequest userGroupUpsertRequest = UserGroupUpsertRequest.builder()
        .name(RandomHelper.alphanumeric(10))
        .roles([Role.IAM_VIEWER.id, Role.IAM_ADMIN.id])
        .build()

    def "ユーザグループリスト取得API: 正常系 IAMの閲覧者がユーザグループリストを取得"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "グループA"
            2  | "グループB"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | role.id
            2             | Role.PURCHASE_REQUEST_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_USER_GROUPS_PATH)
        final response = this.execute(request, HttpStatus.OK, UserGroupsResponse)

        then:
        response.userGroups*.id == [1, 2]
        response.userGroups*.roles == [[role.id], [Role.PURCHASE_REQUEST_ADMIN.id]]

        where:
        role << [Role.IAM_VIEWER, Role.IAM_ADMIN]
    }

    def "ユーザグループリスト取得API: 異常系 IAMの閲覧者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.getRequest(GET_USER_GROUPS_PATH)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザグループリスト取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_USER_GROUPS_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ユーザグループ取得API: 正常系 IAMの閲覧者がユーザグループを取得"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "グループA"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_VIEWER.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        when:
        final request = this.getRequest(String.format(GET_USER_GROUP_PATH, 1))
        final response = this.execute(request, HttpStatus.OK, UserGroupResponse)

        then:
        response.id == 1
        response.name == "グループA"
        response.roles == [Role.IAM_VIEWER.id]
    }

    def "ユーザグループ取得API: 異常系 ユーザグループが存在しない場合は404エラー"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "グループA"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_VIEWER.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        expect:
        final request = this.getRequest(String.format(GET_USER_GROUP_PATH, 2))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

    def "ユーザグループ取得API: 異常系 IAMの閲覧者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.getRequest(String.format(GET_USER_GROUP_PATH, 1))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザグループ取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(String.format(GET_USER_GROUP_PATH, 1))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ユーザグループ作成API: 正常系 IAMの管理者がユーザグループを作成"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        when:
        final request = this.postRequest(CREATE_USER_GROUP_PATH, this.userGroupUpsertRequest)
        this.execute(request, HttpStatus.CREATED)

        then:
        final createdUserGroup = sql.firstRow("select * from user_group where name=:name", [name: this.userGroupUpsertRequest.name])
        createdUserGroup.name == this.userGroupUpsertRequest.name

        final createdUserGroupRoles = sql.rows("SELECT * FROM user_group_role where user_group_id=:user_group_id", [user_group_id: createdUserGroup.id])
        createdUserGroupRoles*.user_group_id == [createdUserGroup.id, createdUserGroup.id]
        createdUserGroupRoles*.role_id == [Role.IAM_VIEWER.id, Role.IAM_ADMIN.id]
    }

    def "ユーザグループ作成API: 異常系 不正なリクエストボディ"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        final requestBody = UserGroupUpsertRequest.builder()
            .name(inputName)
            .roles(inputRoles)
            .build()

        expect:
        final request = this.postRequest(CREATE_USER_GROUP_PATH, requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputName                      | inputRoles           || expectedErrorCode
        RandomHelper.alphanumeric(0)   | [Role.IAM_VIEWER.id] || ErrorCode.INVALID_USER_GROUP_NAME
        RandomHelper.alphanumeric(101) | [Role.IAM_VIEWER.id] || ErrorCode.INVALID_USER_GROUP_NAME
        RandomHelper.alphanumeric(1)   | []                   || ErrorCode.USER_GROUP_ROLES_MUST_NOT_BE_EMPTY
        RandomHelper.alphanumeric(1)   | [-1]                 || ErrorCode.INVALID_USER_GROUP_ROLES
    }

    def "ユーザグループ作成API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.postRequest(CREATE_USER_GROUP_PATH, this.userGroupUpsertRequest)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザグループ作成API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.postRequest(CREATE_USER_GROUP_PATH, this.userGroupUpsertRequest)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ユーザグループ削除API: 正常系 IAMの管理者がユーザグループを削除"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "A"
            2  | "B"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
            2             | Role.IAM_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        when:
        final request = this.deleteRequest(String.format(DELETE_USER_GROUP_PATH, 2))
        this.execute(request, HttpStatus.OK)

        then:
        final userGroups = sql.rows("SELECT * FROM user_group WHERE id=:id", [id: 2])
        userGroups == []
    }

    def "ユーザグループ削除API: 異常系 ユーザグループが存在しない場合は404エラー"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_GROUP_PATH, 2))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

    def "ユーザグループ削除API: 異常系 所属するユーザがいる場合は400エラー"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_GROUP_PATH, 1))
        this.execute(request, new BadRequestException(ErrorCode.USER_GROUP_CANNOT_BE_DELETED))
    }

    def "ユーザグループ削除API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_GROUP_PATH, 1))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザグループ削除API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_GROUP_PATH, 1))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
