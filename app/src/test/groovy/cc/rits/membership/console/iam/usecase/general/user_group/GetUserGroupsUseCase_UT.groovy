package cc.rits.membership.console.iam.usecase.general.user_group

import cc.rits.membership.console.iam.domain.model.UserGroupModel
import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUserGroupsUseCaseの単体テスト
 */
class GetUserGroupsUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUserGroupsUseCase sut

    def "handle: IAMの閲覧者がユーザグループリストを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroups = [
            RandomHelper.mock(UserGroupModel),
            RandomHelper.mock(UserGroupModel),
        ]

        when:
        final result = this.sut.handle(loginUser)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.userGroupRepository.selectAll() >> userGroups
        result == userGroups
    }

    def "handle: IAMの閲覧者以外はユーザグループリストを取得不可"() {
        given:
        final loginUser = Spy(UserModel)

        when:
        this.sut.handle(loginUser)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

}
