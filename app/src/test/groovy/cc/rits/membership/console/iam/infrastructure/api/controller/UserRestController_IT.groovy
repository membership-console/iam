package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.*
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.request.LoginUserPasswordUpdateRequest
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse
import cc.rits.membership.console.iam.infrastructure.api.response.UsersResponse
import org.springframework.http.HttpStatus

/**
 * UserRestControllerの統合テスト
 */
class UserRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/users"
    static final String GET_USERS_PATH = BASE_PATH
    static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me"
    static final String GET_USER_PATH = BASE_PATH + "/%d"
    static final String DELETE_USER_PATH = BASE_PATH + "/%d"
    static final String UPDATE_LOGIN_USER_PASSWORD = BASE_PATH + "/me/password"

    def "ユーザリスト取得API: 正常系 IAMの閲覧者がユーザリストを取得"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
        }
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
        final request = this.getRequest(GET_USERS_PATH)
        final response = this.execute(request, HttpStatus.OK, UsersResponse)

        then:
        response.users*.id == [user.id, 2]
        response.users*.email == [user.email, "user2@example.com"]
    }

    def "ユーザリスト取得API: 異常系 IAMの閲覧者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.getRequest(GET_USERS_PATH)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザリスト取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_USERS_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ログインユーザ取得API: 正常系 ログインユーザを取得する"() {
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
            1             | Role.IAM_ADMIN.id
            1             | Role.PURCHASE_REQUEST_ADMIN.id
            2             | Role.PURCHASE_REQUEST_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
            user.id | 2
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_LOGIN_USER_PATH)
        final response = this.execute(request, HttpStatus.OK, UserResponse)

        then:
        response.id == user.id
        response.firstName == user.firstName
        response.lastName == user.lastName
        response.email == user.email
        response.entranceYear == user.entranceYear
        response.userGroups*.id == [1, 2]
        response.userGroups*.roles == [[Role.IAM_ADMIN.id, Role.PURCHASE_REQUEST_ADMIN.id], [Role.PURCHASE_REQUEST_ADMIN.id]]
    }

    def "ログインユーザ取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_LOGIN_USER_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ユーザ取得API: 正常系 IAMの閲覧者がユーザリストを取得"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
        }
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
        final request = this.getRequest(String.format(GET_USER_PATH, 2))
        final response = this.execute(request, HttpStatus.OK, UserResponse)

        then:
        response.id == 2
        response.email == "user2@example.com"
    }

    def "ユーザ取得API: 異常系 ユーザが存在しない場合は404エラー"() {
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
        final request = this.getRequest(String.format(GET_USER_PATH, 2))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

    def "ユーザ取得API: 異常系 IAMの閲覧者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.getRequest(String.format(GET_USER_PATH, 1))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(String.format(GET_USER_PATH, 1))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ユーザ削除API: 正常系 IAMの管理者がユーザを削除"() {
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
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, user.id))
        this.execute(request, HttpStatus.OK)

        then:
        final users = sql.rows("SELECT * FROM user")
        users == []
    }

    def "ユーザ削除API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | ""
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
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, user.id))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ削除API: 異常系 ユーザが存在しない場合は404エラー"() {
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
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, 0))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

    def "ユーザ削除API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.deleteRequest(String.format(DELETE_USER_PATH, 1))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ログインユーザパスワード更新API: 正常系 ログインユーザのパスワードを更新する"() {
        given:
        final user = this.login()

        final requestBody = LoginUserPasswordUpdateRequest.builder()
            .oldPassword(user.password)
            .newPassword(RandomHelper.password())
            .build()

        when:
        final request = this.putRequest(UPDATE_LOGIN_USER_PASSWORD, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        final updatedUser = sql.firstRow("SELECT password FROM user")
        this.authUtil.isMatchPasswordAndHash(requestBody.newPassword, updatedUser.password as String)
    }

    def "ログインユーザパスワード更新API: 現在のパスワードが間違えている場合は400エラー"() {
        given:
        final user = login()

        final requestBody = LoginUserPasswordUpdateRequest.builder()
            .oldPassword(user.password + "...")
            .newPassword(RandomHelper.password())
            .build()

        expect:
        final request = this.putRequest(UPDATE_LOGIN_USER_PASSWORD, requestBody)
        this.execute(request, new BadRequestException(ErrorCode.INVALID_OLD_PASSWORD))
    }

    def "ログインユーザパスワード更新API: 異常系 リクエストボディのバリデーション"() {
        given:
        final user = login()

        final requestBody = LoginUserPasswordUpdateRequest.builder()
            .oldPassword(user.password)
            .newPassword(newPassword)
            .build()

        expect:
        final request = this.putRequest(UPDATE_LOGIN_USER_PASSWORD, requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        newPassword || expectedErrorCode
        // 8文字未満
        "b9Fj5QY"   || ErrorCode.INVALID_PASSWORD_LENGTH
        // 32文字より多い
        "." * 33    || ErrorCode.INVALID_PASSWORD_LENGTH
        // 大文字・小文字・数字のいずれかが欠如している
        "bFjQYVPg"  || ErrorCode.PASSWORD_IS_TOO_SIMPLE
        "b9fj5qyv"  || ErrorCode.PASSWORD_IS_TOO_SIMPLE
        "B9FJ5QYVP" || ErrorCode.PASSWORD_IS_TOO_SIMPLE
    }

    def "ログインユーザパスワード更新API: 異常系 ログインしていない場合は401エラー"() {
        given:
        final requestBody = LoginUserPasswordUpdateRequest.builder()
            .oldPassword(RandomHelper.password())
            .newPassword(RandomHelper.password())
            .build()

        expect:
        final request = this.putRequest(UPDATE_LOGIN_USER_PASSWORD, requestBody)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
