package cc.rits.membership.console.iam.infrastructure.api.controller.oauth2

import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.controller.AbstractRestController_IT
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse
import cc.rits.membership.console.iam.infrastructure.api.response.UsersResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

/**
 * UserRestControllerの統合テスト
 */
class UserRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/oauth2/users"
    static final String GET_USERS_PATH = BASE_PATH
    static final String GET_USER_PATH = BASE_PATH + "/%d"

    def "ユーザリスト取得API: 正常系 ユーザリストを取得できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            1  | ""         | ""        | "user1@example.com" | ""       | 2000
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
        }
        // @formatter:on

        final accessToken = this.createClient([Scope.USER_READ])

        when:
        final request = this.getRequest(GET_USERS_PATH)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        final response = this.execute(request, HttpStatus.OK, UsersResponse)

        then:
        response.users*.id == [1, 2]
        response.users*.email == ["user1@example.com", "user2@example.com"]
    }

    def "ユーザリスト取得API: 異常系 クライアントがuser:readスコープを持たない場合は403エラー"() {
        given:
        final accessToken = this.createClient([])

        expect:
        final request = this.getRequest(GET_USERS_PATH)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        this.execute(request, new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION))
    }

    def "ユーザリスト取得API: 異常系 クライアントが認証されていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_USERS_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "ユーザ取得API: 正常系 ユーザを取得できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email               | password | entrance_year
            1  | ""         | ""        | "user1@example.com" | ""       | 2000
            2  | ""         | ""        | "user2@example.com" | ""       | 2000
        }
        // @formatter:on

        final accessToken = this.createClient([Scope.USER_READ])

        when:
        final request = this.getRequest(String.format(GET_USER_PATH, 1))
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        final response = this.execute(request, HttpStatus.OK, UserResponse)

        then:
        response.id == 1
        response.email == "user1@example.com"
    }

    def "ユーザ取得API: 異常系 クライアントがuser:readスコープを持たない場合は403エラー"() {
        given:
        final accessToken = this.createClient([])

        expect:
        final request = this.getRequest(String.format(GET_USER_PATH, 1))
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.access_token}")
        this.execute(request, new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION))
    }

    def "ユーザ取得API: 異常系 クライアントが認証されていない場合は401エラー"() {
        expect:
        final request = this.getRequest(String.format(GET_USER_PATH, 1))
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

}
