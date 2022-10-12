package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.exception.BadRequestException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest
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

    def "パスワードリセット要求API: 正常系 メールアドレスが存在しない場合は400エラー"() {
        given:
        final user = this.login()

        final requestBody = RequestPasswordResetRequest.builder()
            .email(user.email + "...")
            .build()

        expect:
        final request = this.postRequest(REQUEST_PASSWORD_RESET_PATH, requestBody)
        this.execute(request, new BadRequestException(ErrorCode.REQUESTED_EMAIL_IS_NOT_EXISTS))
    }

}
