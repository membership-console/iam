package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * ClientRepositoryの単体テスト
 */
class ClientRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    ClientRepositoryImpl sut

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
