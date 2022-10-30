package cc.rits.membership.console.iam.infrastructure.api.controller.general

import cc.rits.membership.console.iam.exception.BadRequestException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.helper.DateHelper
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.controller.AbstractRestController_IT
import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest
import cc.rits.membership.console.iam.infrastructure.api.request.PasswordResetRequest
import cc.rits.membership.console.iam.infrastructure.api.request.RequestPasswordResetRequest
import org.springframework.http.HttpStatus

/**
 * AuthRestControllerの統合テスト
 */
class AuthRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api"
    static final String LOGIN_PATH = BASE_PATH + "/login"
    static final String LOGOUT_PATH = BASE_PATH + "/logout"
    static final String REQUEST_PASSWORD_RESET_PATH = BASE_PATH + "/request_password_reset"
    static final String PASSWORD_RESET_PATH = BASE_PATH + "/password_reset"

    def "ログインAPI: 正常系 ログインに成功するとセッションにユーザ情報が記録される"() {
        given:
        final user = this.login()
        final oldSessionId = this.session.getId()

        final requestBody = LoginRequest.builder()
            .email(user.email)
            .password(user.password)
            .build()

        when:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        !this.session.isInvalid()
        // セッションの有効時間が設定されている
        session.getMaxInactiveInterval() == this.authProperty.sessionTimeout
        // セッションIDが変更されていることを確認 (セッションジャック対策)
        oldSessionId != this.session.getId()
    }

    def "ログインAPI: 異常系 メールアドレスかパスワードが間違っている場合、401エラー"() {
        given:
        final user = this.login()

        when: "メールアドレスが間違っている"
        def requestBody = LoginRequest.builder()
            .email(user.email + "aaa")
            .password(user.password)
            .build()
        def request = this.postRequest(LOGIN_PATH, requestBody)

        then:
        this.execute(request, new UnauthorizedException(ErrorCode.INCORRECT_EMAIL_OR_PASSWORD))

        when: "パスワードが間違っている"
        requestBody = LoginRequest.builder()
            .email(user.email)
            .password(user.password + "aaa")
            .build()
        request = this.postRequest(LOGIN_PATH, requestBody)

        then:
        this.execute(request, new UnauthorizedException(ErrorCode.INCORRECT_EMAIL_OR_PASSWORD))
    }

    def "ログアウトAPI: 正常系 ログアウトするとセッションが廃棄される"() {
        given:
        this.login()

        when:
        final request = this.postRequest(LOGOUT_PATH)
        this.execute(request, HttpStatus.OK)

        then:
        this.session.isInvalid()
    }

    def "ログアウトAPI: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.postRequest(LOGOUT_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "パスワードリセット要求API: 正常系 パスワードリセットを要求"() {
        given:
        final user = this.login()

        final requestBody = RequestPasswordResetRequest.builder()
            .email(user.email)
            .build()

        when:
        final request = this.postRequest(REQUEST_PASSWORD_RESET_PATH, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        final createdPasswordResetToken = sql.firstRow("SELECT * FROM password_reset_token")
        createdPasswordResetToken.user_id == user.id
    }

    def "パスワードリセット要求API: 異常系 メールアドレスが存在しない場合は400エラー"() {
        given:
        final user = this.login()

        final requestBody = RequestPasswordResetRequest.builder()
            .email(user.email + "...")
            .build()

        expect:
        final request = this.postRequest(REQUEST_PASSWORD_RESET_PATH, requestBody)
        this.execute(request, new BadRequestException(ErrorCode.REQUESTED_EMAIL_IS_NOT_EXISTS))
    }

    def "パスワードリセットAPI: 正常系 パスワードをリセット"() {
        given:
        final requestBody = PasswordResetRequest.builder()
            .token(RandomHelper.uuid())
            .newPassword(RandomHelper.password())
            .build()

        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        TableHelper.insert sql, "password_reset_token", {
            id | user_id | token             | expire_at
            1  | 1       | requestBody.token | DateHelper.tomorrow().toString()
        }
        // @formatter:on

        when:
        final request = this.postRequest(PASSWORD_RESET_PATH, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        final updatedUser = sql.firstRow("SELECT * FROM user")
        this.authUtil.isMatchPasswordAndHash(requestBody.newPassword, updatedUser.password as String)

        final passwordResetTokens = sql.rows("SELECT * FROM password_reset_token")
        passwordResetTokens == []
    }

    def "パスワードリセットAPI: 異常系 トークンが無効な場合は400エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        TableHelper.insert sql, "password_reset_token", {
            id | user_id | token      | expire_at
            1  | 1       | inputToken | inputExpireAt.toString()
        }
        // @formatter:on

        final requestBody = PasswordResetRequest.builder()
            .token(isTokenExists ? inputToken : inputToken + "...")
            .newPassword(RandomHelper.password())
            .build()

        expect:
        final request = this.postRequest(PASSWORD_RESET_PATH, requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputToken          | isTokenExists | inputExpireAt          || expectedErrorCode
        RandomHelper.uuid() | false         | DateHelper.tomorrow()  || ErrorCode.INVALID_PASSWORD_RESET_TOKEN
        RandomHelper.uuid() | true          | DateHelper.yesterday() || ErrorCode.INVALID_PASSWORD_RESET_TOKEN
    }

}
