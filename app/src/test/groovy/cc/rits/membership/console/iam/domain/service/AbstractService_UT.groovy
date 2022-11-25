package cc.rits.membership.console.iam.domain.service

import cc.rits.membership.console.iam.AbstractSpecification
import cc.rits.membership.console.iam.domain.repository.IClientRepository
import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository
import cc.rits.membership.console.iam.domain.repository.IUserRepository
import org.spockframework.spring.SpringBean

/**
 * Service単体テストの基底クラス
 */
abstract class AbstractService_UT extends AbstractSpecification {

    @SpringBean
    IUserRepository userRepository = Mock()

    @SpringBean
    IUserGroupRepository userGroupRepository = Mock()

    @SpringBean
    IClientRepository clientRepository = Mock()

}
