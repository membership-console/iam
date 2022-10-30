package cc.rits.membership.console.iam.usecase.general.user_group

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.*
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * DeleteUserGroupUseCaseの単体テスト
 */
class DeleteUserGroupUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DeleteUserGroupUseCase sut

    def "handle: IAMの管理者がユーザグループを削除"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroupId = 1

        when:
        this.sut.handle(loginUser, userGroupId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userGroupRepository.existsById(userGroupId) >> true
        1 * this.userRepository.countByUserGroupId(userGroupId) >> 0
        1 * this.userGroupRepository.deleteById(userGroupId)
    }

    def "handle: IAMの管理者以外はユーザグループリストを削除不可"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroupId = 1

        when:
        this.sut.handle(loginUser, userGroupId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> false
        final BaseException exception = thrown()
        verifyException(exception, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "handle: ユーザグループが存在しない場合は404エラー"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroupId = 1

        when:
        this.sut.handle(loginUser, userGroupId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userGroupRepository.existsById(userGroupId) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

    def "handle: ユーザグループの所属人数が0以外の場合は削除不可"() {
        given:
        final loginUser = Spy(UserModel)
        final userGroupId = 1

        when:
        this.sut.handle(loginUser, userGroupId)

        then:
        1 * loginUser.hasRole(Role.IAM_ADMIN) >> true
        1 * this.userGroupRepository.existsById(userGroupId) >> true
        1 * this.userRepository.countByUserGroupId(userGroupId) >> 1
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.USER_GROUP_CANNOT_BE_DELETED))
    }

}
