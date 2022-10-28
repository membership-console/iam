package cc.rits.membership.console.iam.usecase.user_group

import cc.rits.membership.console.iam.domain.model.UserGroupModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.NotFoundException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.infrastructure.api.request.UserGroupUpsertRequest
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * UpdateUserGroupUseCaseの単体テスト
 */
class UpdateUserGroupUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    UpdateUserGroupUseCase sut

    def "handle: IAMの管理者がユーザグループを更新"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        final requestBody = RandomHelper.mock(UserGroupUpsertRequest)
        requestBody.name = userGroup.name + "..."


        when:
        this.sut.handle(loginUser, userGroup.id, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.of(userGroup)
        1 * this.userGroupService.checkIsNameAlreadyUsed(requestBody.name) >> {}
        1 * this.userGroupRepository.update(_)
    }

    def "handle: IAMの管理者以外はユーザグループを更新不可"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        final requestBody = RandomHelper.mock(UserGroupUpsertRequest)

        when:
        this.sut.handle(loginUser, userGroup.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザグループ名が変更されない場合は、名前の重複チェックをスキップする"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        final requestBody = RandomHelper.mock(UserGroupUpsertRequest)
        requestBody.name = userGroup.name

        when:
        this.sut.handle(loginUser, userGroup.id, requestBody)

        then:
        noExceptionThrown()
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.of(userGroup)
        0 * this.userGroupService.checkIsNameAlreadyUsed(requestBody.name) >> {}
        1 * this.userGroupRepository.update(_)
    }

    def "handle: ユーザグループが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        final requestBody = RandomHelper.mock(UserGroupUpsertRequest)

        when:
        this.sut.handle(loginUser, userGroup.id, requestBody)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

}
