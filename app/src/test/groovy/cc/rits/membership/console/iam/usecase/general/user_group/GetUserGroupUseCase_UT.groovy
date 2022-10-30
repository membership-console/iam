package cc.rits.membership.console.iam.usecase.general.user_group

import cc.rits.membership.console.iam.domain.model.UserGroupModel
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
 * GetUserGroupUseCaseの単体テスト
 */
class GetUserGroupUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUserGroupUseCase sut

    def "handle: IAMの閲覧者がユーザグループを取得"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        when:
        final result = this.sut.handle(loginUser, userGroup.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.of(userGroup)
        result == userGroup
    }

    def "handle: IAMの閲覧者以外はユーザグループリストを取得不可"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        when:
        this.sut.handle(loginUser, userGroup.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザグループが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroup = RandomHelper.mock(UserGroupModel)

        when:
        this.sut.handle(loginUser, userGroup.id)

        then:
        1 * loginUser.hasRole(Role.IAM_VIEWER) >> true
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

}
