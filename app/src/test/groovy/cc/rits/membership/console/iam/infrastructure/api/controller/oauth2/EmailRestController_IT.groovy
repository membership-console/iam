package cc.rits.membership.console.iam.infrastructure.api.controller.oauth2

import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.exception.BadRequestException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.controller.AbstractRestController_IT
import cc.rits.membership.console.iam.infrastructure.api.request.EmailSendRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

/**
 * EmailRestControllerの統合テスト
 */
class EmailRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/oauth2/email"
    static final String SEND_EMAIL_PATH = BASE_PATH + "/send"

    def "メール送信API: 正常系 対象ユーザリストにメールを送信できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            1  | ""         | ""        | "user1@example.com" | ""       | 2000
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
            3  | ""         | ""        | "user3@example.com" | ""       | 2000
        }
        // @formatter:on

        final accessToken = this.createClient([Scope.EMAIL])

        final requestBody = EmailSendRequest.builder()
            .userIds([1, 2])
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        this.execute(request, HttpStatus.OK)
    }

    def "メール送信API: 異常系 リクエストボディのバリデーション"() {
        given:
        final accessToken = this.createClient([Scope.EMAIL])

        final requestBody = EmailSendRequest.builder()
            .userIds(inputUserIds)
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputUserIds || expectedErrorCode
        []           || ErrorCode.USER_IDS_MUST_NOT_BE_EMPTY
    }

    def "メール送信API: 異常系 クライアントがemailスコープを持たない場合は403エラー"() {
        given:
        final accessToken = this.createClient([])

        final requestBody = EmailSendRequest.builder()
            .userIds([1])
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        this.execute(request, new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION))
    }

    def "メール送信API: 異常系 クライアントが認証されていない場合は401エラー"() {
        given:
        final requestBody = EmailSendRequest.builder()
            .userIds([1])
            .subject(RandomHelper.alphanumeric(10))
            .body(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(SEND_EMAIL_PATH, requestBody)
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

}
