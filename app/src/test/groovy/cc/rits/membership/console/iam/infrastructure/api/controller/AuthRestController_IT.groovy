package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.infrastructure.api.request.LoginRequest
import org.springframework.http.HttpStatus

import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME

/**
 * AuthRestControllerの統合テスト
 */
class AuthRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api"
    static final String LOGIN_PATH = BASE_PATH + "/login"

    def "ログインAPI: 正常系 ログインに成功するとセッションにユーザ情報が記録される"() {
        given:
        final user = this.login()

        final requestBody = LoginRequest.builder()
            .email(user.email)
            .password(user.password)
            .build()

        when:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        !this.session.isInvalid()
        this.session.getAttribute(PRINCIPAL_NAME_INDEX_NAME).toString() == user.email
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

}
