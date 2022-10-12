package cc.rits.membership.console.iam.infrastructure.repository

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

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

    def "selectByToken: トークンからパスワードリセットトークンを取得"() {
        given:
        final uuid = RandomHelper.uuid()

        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        TableHelper.insert sql, "password_reset_token", {
            id | user_id | token | expire_at
            1  | 1       | uuid  | LocalDateTime.now().toString()
        }
        // @formatter:on

        when:
        final result = this.sut.selectByToken(uuid)

        then:
        result.isPresent()
        result.get().id == 1
        result.get().userId == 1
        result.get().token == uuid
    }

    def "selectByToken: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectByToken("")

        then:
        result.isEmpty()
    }

    def "deleteById: IDからパスワードリセットトークンを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "user", {
            id | first_name | last_name | email | password | entrance_year
            1  | ""         | ""        | ""    | ""       | 2000
        }
        TableHelper.insert sql, "password_reset_token", {
            id | user_id | token               | expire_at
            1  | 1       | RandomHelper.uuid() | LocalDateTime.now().toString()
            2  | 1       | RandomHelper.uuid() | LocalDateTime.now().toString()
        }
        // @formatter:on

        when:
        this.sut.deleteById(1)

        then:
        final deletedPasswordResetTokens = sql.rows("SELECT * FROM password_reset_token")
        deletedPasswordResetTokens*.id == [2]
    }

}
