package cc.rits.membership.console.iam.usecase.client

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * ReissueCredentialsUseCaseの単体テスト
 */
class ReissueCredentialsUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    ReissueCredentialsUseCase sut

    def "handle: IAMの管理者が認証情報を再発行"() {
        given:
        final loginUser = Spy(UserModel)
        final client = Spy(ClientModel)

        when:
        this.sut.handle(loginUser, client.id)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.of(client)
        1 * client.setClientId(_)
        1 * client.setClientSecret(_)
        1 * this.clientRepository.updateClientIdAndSecret(_)
    }

    def "handle: IAMの管理者以外は認証情報を再発行不可"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        this.sut.handle(loginUser, client.id)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: クライアントが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        when:
        this.sut.handle(loginUser, client.id)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

}
