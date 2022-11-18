package cc.rits.membership.console.iam.usecase.oauth2.user

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Scope
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
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

    def "handle: ユーザリストを取得"() {
        given:
        final loginClient = Spy(ClientModel)
        final user = RandomHelper.mock(UserModel)

        when:
        final result = this.sut.handle(loginClient, user.id)

        then:
        1 * loginClient.hasScope(Scope.USER_READ) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.of(user)
        result == user
    }

    def "handle: 対象スコープを持たない場合はユーザリストを取得不可"() {
        given:
        final loginClient = Spy(ClientModel)
        final user = RandomHelper.mock(UserModel)

        when:
        this.sut.handle(loginClient, user.id)

        then:
        1 * loginClient.hasScope(Scope.USER_READ) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.CLIENT_HAS_NO_PERMISSION))
    }

    def "handle: ユーザが存在しない場合は404エラー"() {
        given:
        final loginClient = Spy(ClientModel)
        final user = RandomHelper.mock(UserModel)

        when:
        this.sut.handle(loginClient, user.id)

        then:
        1 * loginClient.hasScope(Scope.USER_READ) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

}
