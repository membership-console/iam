package cc.rits.membership.console.iam.domain.service

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository
import org.spockframework.spring.SpringBean

/**
 * Service単体テストの基底クラス
 */
abstract class AbstractService_UT extends AbstractSpecification {

    @SpringBean
    UserGroupRepository userGroupRepository = Mock()

}