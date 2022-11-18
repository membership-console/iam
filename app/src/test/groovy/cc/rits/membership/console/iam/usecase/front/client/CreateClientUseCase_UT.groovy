package cc.rits.membership.console.iam.usecase.front.client

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.ClientUpsertRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateClientUseCaseの単体テスト
 */
class CreateClientUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    CreateClientUseCase sut

    def "handle: IAMの管理者がクライアントを作成"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(ClientUpsertRequest)

        when:
        final result = this.sut.handle(loginUser, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientService.checkIsNameAlreadyUsed(requestBody.name) >> {}
        1 * this.clientRepository.insert(_)
        result.name == result.name
    }

    def "handle: IAMの管理者以外はクライアントを作成不可"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(ClientUpsertRequest)

        when:
        this.sut.handle(loginUser, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

}
