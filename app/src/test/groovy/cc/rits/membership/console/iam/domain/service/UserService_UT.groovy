package cc.rits.membership.console.iam.domain.service

import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ConflictException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.helper.RandomHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserServiceの単体テスト
 */
class UserService_UT extends AbstractService_UT {

    @Autowired
    UserService sut

    def "checkIsEmailAlreadyUsed: メールアドレスが使われていない場合は何もしない"() {
        given:
        final email = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsEmailAlreadyUsed(email)

        then:
        1 * this.userRepository.existsByEmail(email) >> false
        noExceptionThrown()
    }

    def "checkIsEmailAlreadyUsed: メールアドレスが使われている場合は409エラー"() {
        given:
        final email = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsEmailAlreadyUsed(email)

        then:
        1 * this.userRepository.existsByEmail(email) >> true
        final BaseException exception = thrown()
        verifyException(exception, new ConflictException(ErrorCode.EMAIL_IS_ALREADY_USED))
    }

}
