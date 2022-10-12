package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel
import cc.rits.membership.console.iam.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * PasswordResetTokenRepositoryの単体テスト
 */
class PasswordResetTokenRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    PasswordResetTokenRepositoryImpl sut

    def "insert: パスワードリセットトークンを作成"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        // @formatter:on

        final passwordResetToken = PasswordResetTokenModel.builder()
            .userId(1)
            .build()

        when:
        this.sut.insert(passwordResetToken)

        then:
        final createdPasswordResetToken = sql.firstRow("SELECT * FROM password_reset_token")
        createdPasswordResetToken.user_id == passwordResetToken.userId
        createdPasswordResetToken.token == passwordResetToken.token
    }

}
