package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * ClientRepositoryの単体テスト
 */
class ClientRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    ClientRepositoryImpl sut

    @Autowired
    PasswordEncoder passwordEncoder

    def "selectAll: クライアントリストを全件取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
            "B" | "B"       | "B"         | [Scope.USER_READ.name].join(",")                  | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final result = this.sut.selectAll()

        then:
        result*.id == ["A", "B"]
        result*.clientId == ["A", "B"]
        result*.name == ["A", "B"]
        result*.scopes == [[Scope.USER_READ, Scope.EMAIL], [Scope.USER_READ]]
    }

    def "selectById: IDからクライアントを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
            "B" | "B"       | "B"         | [Scope.USER_READ.name].join(",")                  | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final result = this.sut.selectById("A")

        then:
        result.isPresent()
        result.get().id == "A"
        result.get().clientId == "A"
        result.get().name == "A"
        result.get().scopes == [Scope.USER_READ, Scope.EMAIL]
    }

    def "selectById: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectById("A")

        then:
        result.isEmpty()
    }

    def "selectByClientId: クライアントIDからクライアントを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "clientA" | "A"         | [Scope.USER_READ.name, Scope.EMAIL.name].join(",") | ""            | ""                            | ""                        | ""              | ""
            "B" | "clientB" | "B"         | [Scope.USER_READ.name].join(",")                  | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final result = this.sut.selectByClientId("clientA")

        then:
        result.isPresent()
        result.get().id == "A"
        result.get().clientId == "clientA"
        result.get().name == "A"
        result.get().scopes == [Scope.USER_READ, Scope.EMAIL]
    }

    def "selectByClientId: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectByClientId("A")

        then:
        result.isEmpty()
    }

    def "insert: クライアントを作成"() {
        given:
        final client = ClientModel.builder()
            .name(RandomHelper.alphanumeric(10))
            .clientId(RandomHelper.alphanumeric(10))
            .clientSecret(RandomHelper.alphanumeric(10))
            .scopes([Scope.USER_READ, Scope.EMAIL])
            .build()

        when:
        this.sut.insert(client)

        then:
        final createdClient = sql.firstRow("SELECT * FROM oauth2_registered_client")
        createdClient.id == client.id
        createdClient.client_name == client.name
        createdClient.client_id == client.clientId
        createdClient.scopes == client.scopes.collect({ it.name }).join(",")
        this.passwordEncoder.matches(client.clientSecret, createdClient.client_secret as String)
    }

    def "updateNameAndScopes: クライアント名とスコープリストを更新"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        final client = ClientModel.builder()
            .id("A")
            .name(RandomHelper.alphanumeric(10))
            .scopes([Scope.USER_READ, Scope.EMAIL])
            .build()

        when:
        this.sut.updateNameAndScopes(client)

        then:
        final updatedClient = sql.firstRow("SELECT * FROM oauth2_registered_client")
        updatedClient.client_name == client.name
        updatedClient.scopes == client.scopes.collect({ it.name }).join(",")
    }

    def "updateClientIdAndSecret: クライアントIDとシークレットを更新"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        final client = ClientModel.builder()
            .id("A")
            .clientId(RandomHelper.alphanumeric(10))
            .clientSecret(RandomHelper.alphanumeric(10))
            .build()

        when:
        this.sut.updateClientIdAndSecret(client)

        then:
        final updatedClient = sql.firstRow("SELECT * FROM oauth2_registered_client")
        updatedClient.client_id == client.clientId
        this.passwordEncoder.matches(client.clientSecret, updatedClient.client_secret as String)
    }

    def "existsByName: クライアント名の存在確認"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final result = this.sut.existsByName(inputName)

        then:
        result == expectedResult

        where:
        inputName || expectedResult
        "A"       || true
        "B"       || false
    }

    def "existsById: IDの存在確認"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes                                             | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final result = this.sut.existsById(inputId)

        then:
        result == expectedResult

        where:
        inputId || expectedResult
        "A"     || true
        "B"     || false
    }

    def "deleteById: IDのからクライアントを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "oauth2_registered_client", {
            id  | client_id | client_name | scopes | client_secret | client_authentication_methods | authorization_grant_types | client_settings | token_settings
            "A" | "A"       | "A"         | ""     | ""            | ""                            | ""                        | ""              | ""
            "B" | "B"       | "B"         | ""     | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        this.sut.deleteById("A")

        then:
        final clients = sql.rows("SELECT * FROM oauth2_registered_client")
        clients*.id == ["B"]
    }

}
