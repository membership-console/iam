package cc.rits.membership.console.iam.usecase.general.client

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * DeleteClientUseCaseの単体テスト
 */
class DeleteClientUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DeleteClientUseCase sut

    def "handle: IAMの管理者がクライアントを削除"() {
        given:
        final loginUser = Spy(UserModel)
        final id = ""

        when:
        this.sut.handle(loginUser, id)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.existsById(id) >> true
        1 * this.clientRepository.deleteById(id)
    }

    def "handle: IAMの管理者以外はクライアントリストを削除不可"() {
        given:
        final loginUser = Spy(UserModel)
        final id = ""

        when:
        this.sut.handle(loginUser, id)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: クライアントが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final id = ""

        when:
        this.sut.handle(loginUser, id)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.existsById(id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

}
