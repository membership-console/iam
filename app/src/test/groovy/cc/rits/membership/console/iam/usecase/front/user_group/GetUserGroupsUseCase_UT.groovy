package cc.rits.membership.console.iam.usecase.front.user_group

import cc.rits.membership.console.iam.domain.model.UserGroupModel
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUserGroupsUseCaseの単体テスト
 */
class GetUserGroupsUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUserGroupsUseCase sut

    def "handle: ユーザグループリストを取得"() {
        given:
        final userGroups = [
            RandomHelper.mock(UserGroupModel),
            RandomHelper.mock(UserGroupModel),
        ]

        when:
        final result = this.sut.handle()

        then:
        1 * this.userGroupRepository.selectAll() >> userGroups
        result == userGroups
    }

}
