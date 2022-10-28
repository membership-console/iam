package cc.rits.membership.console.iam.infrastructure.repository


import cc.rits.membership.console.iam.enums.Scope
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
            "B" | "B"       | "B"         | [Scope.USER_WRITE.name].join(",")                  | ""            | ""                            | ""                        | ""              | ""
        }
        // @formatter:on

        when:
        final result = this.sut.selectAll()

        then:
        result*.id == ["A", "B"]
        result*.clientId == ["A", "B"]
        result*.name == ["A", "B"]
        result*.scopes == [[Scope.USER_READ, Scope.EMAIL], [Scope.USER_WRITE]]
    }

}
