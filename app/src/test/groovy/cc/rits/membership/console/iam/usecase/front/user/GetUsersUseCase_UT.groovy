package cc.rits.membership.console.iam.usecase.front.user

import cc.rits.membership.console.iam.domain.model.UserModel
import cc.rits.membership.console.iam.helper.RandomHelper
import cc.rits.membership.console.iam.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUsersUseCaseの単体テスト
 */
class GetUsersUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUsersUseCase sut

    def "handle: ユーザリストを取得"() {
        given:
        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        final result = this.sut.handle()

        then:
        1 * this.userRepository.selectAll() >> users
        result == users
    }

}
