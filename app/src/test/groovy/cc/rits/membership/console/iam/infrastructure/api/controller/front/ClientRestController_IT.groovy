package cc.rits.membership.console.iam.infrastructure.api.controller.front

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.exception.*
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.controller.AbstractRestController_IT
import cc.rits.membership.console.iam.infrastructure.api.request.ClientUpsertRequest
import cc.rits.membership.console.iam.infrastructure.api.response.ClientCredentialsResponse
import cc.rits.membership.console.iam.infrastructure.api.response.ClientResponse
import cc.rits.membership.console.iam.infrastructure.api.response.ClientsResponse
import org.springframework.http.HttpStatus
import spock.lang.Shared

/**
 * ClientRestControllerの統合テスト
 */
class ClientRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/clients"
    static final String GET_CLIENTS_PATH = BASE_PATH
    static final String GET_CLIENT_PATH = BASE_PATH + "/%s"
    static final String CREATE_CLIENTS_PATH = BASE_PATH
    static final String UPDATE_CLIENTS_PATH = BASE_PATH + "/%s"
    static final String DELETE_CLIENTS_PATH = BASE_PATH + "/%s"
    static final String REISSUE_CREDENTIALS_PATH = BASE_PATH + "/%s/reissue"

    @Shared
    ClientUpsertRequest clientUpsertRequest = ClientUpsertRequest.builder()
        .name(RandomHelper.alphanumeric(10))
        .scopes([Scope.USER_READ.name, Scope.EMAIL.name])
        .build()

    def "クライアントリスト取得API: 正常系 クライアントリストを取得"() {
        given:
        this.login()

        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
            "B" | "B"       | "B"         | [Scope.USER_READ.name].join(",")                   | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_CLIENTS_PATH)
        final response = this.execute(request, HttpStatus.OK, ClientsResponse)

        then:
        response.clients*.id == ["A", "B"]
        response.clients*.clientId == ["A", "B"]
        response.clients*.name == ["A", "B"]
        response.clients*.scopes == [[Scope.USER_READ.name, Scope.EMAIL.name], [Scope.USER_READ.name]]
    }

    def "クライアントリスト取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_CLIENTS_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "クライアント取得API: 正常系 クライアントを取得"() {
        given:
        this.login()

        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final request = this.getRequest(String.format(GET_CLIENT_PATH, "A"))
        final response = this.execute(request, HttpStatus.OK, ClientResponse)

        then:
        response.id == "A"
        response.clientId == "A"
        response.name == "A"
        response.scopes == [Scope.USER_READ.name, Scope.EMAIL.name]
    }

    def "クライアント取得API: 異常系 クライアントが存在しない場合は404エラー"() {
        given:
        this.login()

        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        expect:
        final request = this.getRequest(String.format(GET_CLIENT_PATH, "B"))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

    def "クライアント取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(String.format(GET_CLIENT_PATH, "A"))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "クライアント作成API: 正常系 IAMの管理者がクライアントを作成"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        when:
        final request = this.postRequest(CREATE_CLIENTS_PATH, this.clientUpsertRequest)
        final response = this.execute(request, HttpStatus.CREATED, ClientCredentialsResponse)

        then:
        final createdClient = sql.firstRow("SELECT * FROM oauth2_registered_client")
        createdClient.client_name == this.clientUpsertRequest.name
        createdClient.scopes == this.clientUpsertRequest.scopes.join(",")
        createdClient.client_id == response.clientId
        this.authUtil.isMatchPasswordAndHash(response.clientSecret, createdClient.client_secret as String)
    }

    def "クライアント作成API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.postRequest(CREATE_CLIENTS_PATH, this.clientUpsertRequest)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "クライアント作成API: 異常系 リクエストボディのバリデーション"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        final requestBody = ClientUpsertRequest.builder()
            .name(inputName)
            .scopes(inputScopes)
            .build()

        expect:
        final request = this.postRequest(CREATE_CLIENTS_PATH, requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputName                      | inputScopes            || expectedErrorCode
        RandomHelper.alphanumeric(0)   | [Scope.USER_READ.name] || ErrorCode.INVALID_CLIENT_NAME
        RandomHelper.alphanumeric(101) | [Scope.USER_READ.name] || ErrorCode.INVALID_CLIENT_NAME
        RandomHelper.alphanumeric(1)   | []                     || ErrorCode.CLIENT_SCOPES_MUST_NOT_BE_EMPTY
        RandomHelper.alphanumeric(1)   | [""]                   || ErrorCode.INVALID_CLIENT_SCOPES
    }

    def "クライアント作成API: 異常系 クライアント名が既に使われている場合は409エラー"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name                   | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | this.clientUpsertRequest.name | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        expect:
        final request = this.postRequest(CREATE_CLIENTS_PATH, this.clientUpsertRequest)
        this.execute(request, new ConflictException(ErrorCode.CLIENT_NAME_IS_ALREADY_USED))
    }

    def "クライアント作成API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.postRequest(CREATE_CLIENTS_PATH, this.clientUpsertRequest)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "クライアント更新API: 正常系 IAMの管理者がクライアントを更新"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        final requestBody = ClientUpsertRequest.builder()
            .name(inputName)
            .scopes(this.clientUpsertRequest.scopes)
            .build()

        when:
        final request = this.putRequest(String.format(UPDATE_CLIENTS_PATH, "A"), requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        final updatedClient = sql.firstRow("SELECT * FROM oauth2_registered_client")
        updatedClient.client_name == requestBody.name
        updatedClient.scopes == requestBody.scopes.join(",")

        where:
        inputName << ["A", "B"]
    }

    def "クライアント更新API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        this.login()

        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        expect:
        final request = this.putRequest(String.format(UPDATE_CLIENTS_PATH, "A"), this.clientUpsertRequest)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "クライアント更新API: 異常系 クライアントが存在しない場合は404エラー"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        expect:
        final request = this.putRequest(String.format(UPDATE_CLIENTS_PATH, "A"), this.clientUpsertRequest)
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

    def "クライアント更新API: 異常系 リクエストボディのバリデーション"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        final requestBody = ClientUpsertRequest.builder()
            .name(inputName)
            .scopes(inputScopes)
            .build()

        expect:
        final request = this.putRequest(String.format(UPDATE_CLIENTS_PATH, "A"), requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputName                      | inputScopes            || expectedErrorCode
        RandomHelper.alphanumeric(0)   | [Scope.USER_READ.name] || ErrorCode.INVALID_CLIENT_NAME
        RandomHelper.alphanumeric(101) | [Scope.USER_READ.name] || ErrorCode.INVALID_CLIENT_NAME
        RandomHelper.alphanumeric(1)   | []                     || ErrorCode.CLIENT_SCOPES_MUST_NOT_BE_EMPTY
        RandomHelper.alphanumeric(1)   | [""]                   || ErrorCode.INVALID_CLIENT_SCOPES
    }

    def "クライアント更新API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.putRequest(String.format(UPDATE_CLIENTS_PATH, "A"), this.clientUpsertRequest)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "クライアント削除API: 正常系 IAMの管理者がクライアントを削除"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
            "B" | "B"       | "B"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final request = this.deleteRequest(String.format(DELETE_CLIENTS_PATH, "A"))
        this.execute(request, HttpStatus.OK)

        then:
        final clients = sql.rows("SELECT * FROM oauth2_registered_client")
        clients*.id == ["B"]
    }

    def "クライアント削除API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.deleteRequest(String.format(DELETE_CLIENTS_PATH, "A"))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "クライアント削除API: 異常系 クライアントが存在しない場合は404エラー"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        expect:
        final request = this.deleteRequest(String.format(DELETE_CLIENTS_PATH, "A"))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

    def "クライアント削除API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.deleteRequest(String.format(DELETE_CLIENTS_PATH, "A"))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "クライアント認証情報再発行API: 正常系 IAMの管理者が認証情報を再発行"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final request = this.postRequest(String.format(REISSUE_CREDENTIALS_PATH, "A"))
        final response = this.execute(request, HttpStatus.OK, ClientCredentialsResponse)

        then:
        final updatedClient = sql.firstRow("SELECT * FROM oauth2_registered_client")
        updatedClient.client_id == response.clientId
        this.authUtil.isMatchPasswordAndHash(response.clientSecret, updatedClient.client_secret as String)
    }

    def "クライアント認証情報再発行API: 異常系 IAMの管理者以外は403エラー"() {
        given:
        this.login()

        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        expect:
        final request = this.postRequest(String.format(REISSUE_CREDENTIALS_PATH, "A"))
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "クライアント認証情報再発行API: 異常系 クライアントが存在しない場合は404エラー"() {
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
        TableHelper.insert sql, "r_user_group_user", {
            user_id | user_group_id
            user.id | 1
        }
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        expect:
        final request = this.postRequest(String.format(REISSUE_CREDENTIALS_PATH, "B"))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

    def "クライアント認証情報再発行API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.postRequest(String.format(REISSUE_CREDENTIALS_PATH, "A"))
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
