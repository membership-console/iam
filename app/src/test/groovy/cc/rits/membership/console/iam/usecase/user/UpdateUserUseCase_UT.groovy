package cc.rits.membership.console.iam.usecase.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.UserUpdateRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * UpdateUserUseCaseの単体テスト
 */
class UpdateUserUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    UpdateUserUseCase sut

    def "handle: IAMの管理者がユーザを更新"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        final requestBody = RandomHelper.mock(UserUpdateRequest)
        requestBody.email = user.email + "..."

        when:
        this.sut.handle(loginUser, user.id, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.of(user)
        1 * this.userService.checkIsEmailAlreadyUsed(requestBody.email) >> {}
        1 * this.userGroupRepository.existsByIds(requestBody.userGroupIds) >> true
        1 * this.userGroupRepository.selectByIds(requestBody.userGroupIds) >> []
        1 * this.userRepository.update(_)
    }

    def "handle: IAMの管理者以外はユーザを更新不可"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        final requestBody = RandomHelper.mock(UserUpdateRequest)


        when:
        this.sut.handle(loginUser, user.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザグループが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        final requestBody = RandomHelper.mock(UserUpdateRequest)
        requestBody.email = user.email + "..."

        when:
        this.sut.handle(loginUser, user.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.of(user)
        1 * this.userService.checkIsEmailAlreadyUsed(requestBody.email) >> {}
        1 * this.userGroupRepository.existsByIds(requestBody.userGroupIds) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

    def "handle: ユーザが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        final requestBody = RandomHelper.mock(UserUpdateRequest)

        when:
        this.sut.handle(loginUser, user.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER))
    }

    def "handle: メールアドレスが変更されない場合は、名前の重複チェックをスキップする"() {
        given:
        final loginUser = Spy(UserModel)
        final user = RandomHelper.mock(UserModel)

        final requestBody = RandomHelper.mock(UserUpdateRequest)
        requestBody.email = user.email

        when:
        this.sut.handle(loginUser, user.id, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userRepository.selectById(user.id) >> Optional.of(user)
        0 * this.userService.checkIsEmailAlreadyUsed(requestBody.email) >> {}
        1 * this.userGroupRepository.existsByIds(requestBody.userGroupIds) >> true
        1 * this.userGroupRepository.selectByIds(requestBody.userGroupIds) >> []
        1 * this.userRepository.update(_)
    }

}
