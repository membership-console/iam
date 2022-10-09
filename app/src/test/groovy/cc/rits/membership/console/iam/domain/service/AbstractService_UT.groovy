package cc.rits.membership.console.iam.domain.service

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository
import cc.rits.membership.console.iam.domain.repository.UserRepository
import org.spockframework.spring.SpringBean

/**
 * Service単体テストの基底クラス
 */
abstract class AbstractService_UT extends AbstractSpecification {

    @SpringBean
    UserRepository userRepository = Mock()

    @SpringBean
    UserGroupRepository userGroupRepository = Mock()

}
