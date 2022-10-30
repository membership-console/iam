package cc.rits.membership.console.iam.usecase.general.user

import cc.rits.membership.console.iam.domain.model.UserGroupModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.UserCreateRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateUserUseCaseの単体テスト
 */
class CreateUserUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    CreateUserUseCase sut

    def "handle: IAMの管理者がユーザを作成"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(UserCreateRequest)

        when:
        this.sut.handle(loginUser, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userService.checkIsEmailAlreadyUsed(requestBody.email) >> {}
        1 * this.userGroupRepository.existsByIds(requestBody.userGroupIds) >> true
        1 * this.userGroupRepository.selectByIds(requestBody.userGroupIds) >> requestBody.userGroupIds.collect { RandomHelper.mock(UserGroupModel) }
        1 * this.userRepository.insert(_)
    }

    def "handle: IAMの管理者以外はユーザを作成不可"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(UserCreateRequest)


        when:
        this.sut.handle(loginUser, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザグループが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final requestBody = RandomHelper.mock(UserCreateRequest)

        when:
        this.sut.handle(loginUser, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userService.checkIsEmailAlreadyUsed(requestBody.email) >> {}
        1 * this.userGroupRepository.existsByIds(requestBody.userGroupIds) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

}
