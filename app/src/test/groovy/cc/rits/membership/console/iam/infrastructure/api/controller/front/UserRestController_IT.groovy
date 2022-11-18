package cc.rits.membership.console.iam.infrastructure.api.controller.front

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.*
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.controller.AbstractRestController_IT
import cc.rits.membership.console.iam.infrastructure.api.request.LoginUserPasswordUpdateRequest
import cc.rits.membership.console.iam.infrastructure.api.request.UserCreateRequest
import cc.rits.membership.console.iam.infrastructure.api.request.UserUpdateRequest
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse
import cc.rits.membership.console.iam.infrastructure.api.response.UsersResponse
import org.springframework.http.HttpStatus
import spock.lang.Shared

import java.time.LocalDateTime

/**
 * UserRestControllerの統合テスト
 */
class UserRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/users"
    static final String GET_USERS_PATH = BASE_PATH
    static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me"
    static final String GET_USER_PATH = BASE_PATH + "/%d"
    static final String CREATE_USER_PATH = BASE_PATH
    static final String UPDATE_USER_PATH = BASE_PATH + "/%d"
    static final String DELETE_USER_PATH = BASE_PATH + "/%d"
    static final String UPDATE_LOGIN_USER_PASSWORD = BASE_PATH + "/me/password"

    @Shared
    UserCreateRequest userCreateRequest

    @Shared
    UserUpdateRequest userUpdateRequest

    def setup() {
        this.userCreateRequest = UserCreateRequest.builder()
            .firstName(RandomHelper.alphanumeric(10))
            .lastName(RandomHelper.alphanumeric(10))
            .email(RandomHelper.email())
            .password(RandomHelper.password())
            .entranceYear(LocalDateTime.now().year)
            .userGroupIds([1])
            .build()

        this.userUpdateRequest = UserUpdateRequest.builder()
            .firstName(RandomHelper.alphanumeric(10))
            .lastName(RandomHelper.alphanumeric(10))
            .email(RandomHelper.email())
            .entranceYear(LocalDateTime.now().year)
            .userGroupIds([1])
            .build()
    }

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

    def "ユーザ作成API: 正常系 IAMの管理者がユーザを作成"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "1"
            2  | "2"
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

        this.userCreateRequest.userGroupIds = [1, 2]

        when:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        this.execute(request, HttpStatus.CREATED)

        then:
        final createdUser = sql.firstRow("SELECT * FROM user WHERE id != :id", [id: user.id])
        createdUser.first_name == this.userCreateRequest.firstName
        createdUser.last_name == this.userCreateRequest.lastName
        createdUser.email == this.userCreateRequest.email
        createdUser.entrance_year == this.userCreateRequest.entranceYear
        this.authUtil.isMatchPasswordAndHash(this.userCreateRequest.password, createdUser.password as String)

        final created_r__user__user_group_list = sql.rows("SELECT * FROM r__user__user_group WHERE user_id = :user_id", [user_id: createdUser.id])
        created_r__user__user_group_list*.user_id == this.userCreateRequest.userGroupIds.collect { createdUser.id }
        created_r__user__user_group_list*.user_group_id == this.userCreateRequest.userGroupIds
    }

    def "ユーザ作成API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "1"
            2  | "2"
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
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ作成API: 異常系 ユーザグループが存在しない場合は404エラー"() {
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

        this.userCreateRequest.userGroupIds = [0]

        expect:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

    def "ユーザ作成API: 異常系 メールアドレスが既に使われている場合は409エラー"() {
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

        this.userCreateRequest.email = user.email
        this.userCreateRequest.userGroupIds = [1]

        expect:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        this.execute(request, new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED))
    }

    def "ユーザ作成API: 異常系 リクエストボディのバリデーション"() {
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

        final requestBody = UserCreateRequest.builder()
            .firstName(inputFirstName)
            .lastName(inputLastName)
            .email(inputEmail)
            .password(inputPassword)
            .entranceYear(inputEntranceYear)
            .userGroupIds(inputUserGroupIds)
            .build()

        expect:
        final request = this.postRequest(CREATE_USER_PATH, requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputFirstName                 | inputLastName                  | inputEmail           | inputPassword           | inputEntranceYear            | inputUserGroupIds || expectedErrorCode
        RandomHelper.alphanumeric(0)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | RandomHelper.password() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_FIRST_NAME
        RandomHelper.alphanumeric(256) | RandomHelper.alphanumeric(1)   | RandomHelper.email() | RandomHelper.password() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_FIRST_NAME
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(0)   | RandomHelper.email() | RandomHelper.password() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_LAST_NAME
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(256) | RandomHelper.email() | RandomHelper.password() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_LAST_NAME
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | ""                   | RandomHelper.password() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_EMAIL
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | ""                      | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_PASSWORD_LENGTH
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | "." * 33                | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_PASSWORD_LENGTH
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | "." * 8                 | LocalDateTime.now().year     | [1]               || ErrorCode.PASSWORD_IS_TOO_SIMPLE
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | RandomHelper.password() | LocalDateTime.now().year + 1 | [1]               || ErrorCode.INVALID_USER_ENTRANCE_YEAR
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | RandomHelper.password() | LocalDateTime.now().year     | []                || ErrorCode.USER_GROUPS_MUST_NOT_BE_EMPTY
    }

    def "ユーザ作成API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.postRequest(CREATE_USER_PATH, this.userCreateRequest)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ユーザ更新API: 正常系 IAMの管理者がユーザを更新"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "所属解除するグループ"
            2  | "継続して所属するグループ"
            3  | "新規所属するグループ"
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            1       | 1
            1       | 2
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
        }
        // @formatter:on

        this.userUpdateRequest.email = inputEmail
        this.userUpdateRequest.userGroupIds = [2, 3]

        when:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, user.id), this.userUpdateRequest)
        this.execute(request, HttpStatus.OK)

        then:
        final updatedUser = sql.firstRow("SELECT * FROM user")
        updatedUser.first_name == this.userUpdateRequest.firstName
        updatedUser.last_name == this.userUpdateRequest.lastName
        updatedUser.email == this.userUpdateRequest.email
        updatedUser.entrance_year == this.userUpdateRequest.entranceYear

        final updated_r__user__user_group_list = sql.rows("SELECT * FROM r__user__user_group")
        updated_r__user__user_group_list*.user_id == this.userUpdateRequest.userGroupIds.collect { updatedUser.id }
        updated_r__user__user_group_list*.user_group_id == this.userUpdateRequest.userGroupIds

        where:
        inputEmail << [AbstractRestController_IT.LOGIN_USER_EMAIL, "updated" + AbstractRestController_IT.LOGIN_USER_EMAIL]
    }

    def "ユーザ更新API: 異常系 IAMの管理者以外は403エラー"() {
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
        final request = this.putRequest(String.format(UPDATE_USER_PATH, user.id), this.userUpdateRequest)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザ更新API: 異常系 ユーザ、もしくはユーザグループが存在しない場合は404エラー"() {
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

        this.userUpdateRequest.userGroupIds = inputUserGroupIds

        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, inputUserId), this.userUpdateRequest)
        this.execute(request, new NotFoundException(expectedErrorCode))

        where:
        inputUserId | inputUserGroupIds || expectedErrorCode
        0           | [1]               || ErrorCode.NOT_FOUND_USER
        1           | [0]               || ErrorCode.NOT_FOUND_USER_GROUP
    }

    def "ユーザ更新API: 異常系 メールアドレスが既に使われている場合は409エラー"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
        }
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

        this.userUpdateRequest.email = "user2@example.com"

        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, user.id), this.userUpdateRequest)
        this.execute(request, new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED))
    }

    def "ユーザ更新API: 異常系 リクエストボディのバリデーション"() {
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

        final requestBody = UserUpdateRequest.builder()
            .firstName(inputFirstName)
            .lastName(inputLastName)
            .email(inputEmail)
            .entranceYear(inputEntranceYear)
            .userGroupIds(inputUserGroupIds)
            .build()

        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, user.id), requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputFirstName                 | inputLastName                  | inputEmail           | inputEntranceYear            | inputUserGroupIds || expectedErrorCode
        RandomHelper.alphanumeric(0)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_FIRST_NAME
        RandomHelper.alphanumeric(256) | RandomHelper.alphanumeric(1)   | RandomHelper.email() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_FIRST_NAME
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(0)   | RandomHelper.email() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_LAST_NAME
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(256) | RandomHelper.email() | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_LAST_NAME
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | ""                   | LocalDateTime.now().year     | [1]               || ErrorCode.INVALID_USER_EMAIL
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | LocalDateTime.now().year + 1 | [1]               || ErrorCode.INVALID_USER_ENTRANCE_YEAR
        RandomHelper.alphanumeric(1)   | RandomHelper.alphanumeric(1)   | RandomHelper.email() | LocalDateTime.now().year     | []                || ErrorCode.USER_GROUPS_MUST_NOT_BE_EMPTY
    }

    def "ユーザ更新API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.putRequest(String.format(UPDATE_USER_PATH, 1), this.userUpdateRequest)
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
