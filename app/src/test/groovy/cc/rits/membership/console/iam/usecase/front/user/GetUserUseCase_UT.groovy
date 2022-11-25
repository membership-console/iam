package cc.rits.membership.console.iam.usecase.front.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUserUseCaseの単体テスト
 */
class GetUserUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUserUseCase sut

    def "handle: ユーザを取得"() {
        given:
        final user = RandomHelper.mock(UserModel)

        when:
        final result = this.sut.handle(user.id)

        then:
        1 * this.userRepository.selectById(user.id) >> Optional.of(user)
        result == user
    }

    def "handle: ユーザが存在しない場合は404エラー"() {
        given:
        final user = RandomHelper.mock(UserModel)

        when:
        this.sut.handle(user.id)

        then:
        1 * this.userRepository.selectById(user.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

}
