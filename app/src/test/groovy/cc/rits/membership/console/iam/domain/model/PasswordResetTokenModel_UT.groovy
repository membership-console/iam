package cc.rits.membership.console.iam.domain.model

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.helper.DateHelper

import java.time.LocalDateTime

/**
 * PasswordResetTokenModelの単体テスト
 */
class PasswordResetTokenModel_UT extends AbstractSpecification {

    def "builder: インスタンス生成時にtokenと有効期限が自動でセットされる"() {
        when:
        final passwordResetToken = PasswordResetTokenModel.builder()
            .build()

        then:
        passwordResetToken.token.length() == 36
        passwordResetToken.expireAt.isAfter(LocalDateTime.now())
    }

    def "inValid: パスワードリセットトークンが有効かチェック"() {
        given:
        final passwordResetToken = PasswordResetTokenModel.builder()
            .expireAt(inputExpireAt)
            .build()

        when:
        final result = passwordResetToken.isValid()

        then:
        result == expectedResult

        where:
        inputExpireAt          || expectedResult
        DateHelper.tomorrow()  || true
        DateHelper.yesterday() || false
    }

}
