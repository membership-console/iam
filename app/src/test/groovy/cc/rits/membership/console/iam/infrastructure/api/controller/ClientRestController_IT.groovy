package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.response.ClientsResponse
import org.springframework.http.HttpStatus

/**
 * ClientRestControllerの統合テスト*/
class ClientRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/clients"
    static final String GET_CLIENTS_PATH = BASE_PATH

    def "クライアントリスト取得API: 正常系 IAMの閲覧者がクライアントリストを取得"() {
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
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
            "B" | "B"       | "B"         | [Scope.USER_READ.name].join(",")                  | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        println(sql.rows("SELECT * FROM oauth2_registered_client"))

        when:
        final request = this.getRequest(GET_CLIENTS_PATH)
        final response = this.execute(request, HttpStatus.OK, ClientsResponse)

        then:
        response.clients*.id == ["A", "B"]
        response.clients*.clientId == ["A", "B"]
        response.clients*.name == ["A", "B"]
        response.clients*.scopes == [[Scope.USER_READ.name, Scope.EMAIL.name], [Scope.USER_READ.name]]
    }

    def "クライアントリスト取得API: 正常系 IAMの閲覧者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.getRequest(GET_CLIENTS_PATH)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "クライアントリスト取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_CLIENTS_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
