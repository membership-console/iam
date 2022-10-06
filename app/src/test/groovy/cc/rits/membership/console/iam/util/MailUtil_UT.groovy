package cc.rits.membership.console.iam.util

import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.InternalServerErrorException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.property.SendGridProperty
import org.springframework.beans.factory.annotation.Autowired

/**
 * MailUtilの単体テスト
 */
class MailUtil_UT extends AbstractUtil_UT {

    @Autowired
    MailUtil sut

    def "send: メールを送信"() {
        when:
        this.sut.send([RandomHelper.email()], RandomHelper.alphanumeric(10), RandomHelper.alphanumeric(10))

        then:
        1 * this.sendGridProperty.enabled >> false
        noExceptionThrown()
    }

    def "send: 送信に失敗した場合は500エラー"() {
        when:
        this.sut.send([RandomHelper.email()], RandomHelper.alphanumeric(10), RandomHelper.alphanumeric(10))

        then:
        1 * this.sendGridProperty.enabled >> true
        2 * this.sendGridProperty.from >> RandomHelper.mock(SendGridProperty.From)
        1 * this.sendGridProperty.apiKey >> RandomHelper.alphanumeric(10)
        final BaseException exception = thrown()
        verifyException(exception, new InternalServerErrorException(ErrorCode.FAILED_TO_SEND_MAIL))
    }

}
