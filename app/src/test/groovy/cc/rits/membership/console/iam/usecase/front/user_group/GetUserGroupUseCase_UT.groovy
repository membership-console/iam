package cc.rits.membership.console.iam.usecase.front.user_group

import cc.rits.membership.console.iam.domain.model.UserGroupModel
import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ErrorCode
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

    def "handle: ユーザグループを取得"() {
        given:
        final userGroup = RandomHelper.mock(UserGroupModel)

        when:
        final result = this.sut.handle(userGroup.id)

        then:
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.of(userGroup)
        result == userGroup
    }

    def "handle: ユーザグループが存在しない場合は404エラー"() {
        given:
        final userGroup = RandomHelper.mock(UserGroupModel)

        when:
        this.sut.handle(userGroup.id)

        then:
        1 * this.userGroupRepository.selectById(userGroup.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER_GROUP))
    }

}
