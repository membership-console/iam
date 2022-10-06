package cc.rits.membership.console.iam.domain.service

import cc.rits.membership.console.iam.exception.BaseException
import cc.rits.membership.console.iam.exception.ConflictException
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.helper.RandomHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserGroupServiceの単体テスト
 */
class UserGroupService_UT extends AbstractService_UT {

    @Autowired
    UserGroupService sut

    def "checkIsNameAlreadyUsed: ユーザグループ名が使われていない場合は何もしない"() {
        given:
        final name = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsNameAlreadyUsed(name)

        then:
        1 * this.userGroupRepository.existsByName(name) >> false
        noExceptionThrown()
    }

    def "checkIsNameAlreadyUsed: ユーザグループ名が使われている場合は409エラー"() {
        given:
        final name = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsNameAlreadyUsed(name)

        then:
        1 * this.userGroupRepository.existsByName(name) >> true
        final BaseException exception = thrown()
        verifyException(exception, new ConflictException(ErrorCode.USER_GROUP_NAME_IS_ALREADY_USED))
    }

}
