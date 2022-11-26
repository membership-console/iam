package cc.rits.membership.console.iam.usecase.front.client

import cc.rits.membership.console.iam.domain.model.ClientModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.ClientUpsertRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * UpdateClientUseCaseの単体テスト
 */
class UpdateClientUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    UpdateClientUseCase sut

    def "handle: IAMの管理者がクライアントを更新"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        final requestBody = RandomHelper.mock(ClientUpsertRequest)
        requestBody.name = client.name + "..."

        when:
        this.sut.handle(loginUser, client.id, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.of(client)
        1 * this.clientService.checkIsNameAlreadyUsed(requestBody.name) >> {}
        1 * this.clientRepository.updateNameAndScopes(_)
    }

    def "handle: IAMの管理者以外はクライアントを更新不可"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        final requestBody = RandomHelper.mock(ClientUpsertRequest)

        when:
        this.sut.handle(loginUser, client.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: クライアント名が変更されない場合は、名前の重複チェックをスキップする"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        final requestBody = RandomHelper.mock(ClientUpsertRequest)
        requestBody.name = client.name

        when:
        this.sut.handle(loginUser, client.id, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.of(client)
        0 * this.clientService.checkIsNameAlreadyUsed(requestBody.name) >> {}
        1 * this.clientRepository.updateNameAndScopes(_)
    }

    def "handle: クライアントが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final client = RandomHelper.mock(ClientModel)

        final requestBody = RandomHelper.mock(ClientUpsertRequest)
        requestBody.name = client.name

        when:
        this.sut.handle(loginUser, client.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.clientRepository.selectById(client.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_CLIENT))
    }

}
