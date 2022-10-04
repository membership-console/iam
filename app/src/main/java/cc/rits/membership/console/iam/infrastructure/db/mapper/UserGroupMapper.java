package cc.rits.membership.console.iam.infrastructure.db.mapper;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.infrastructure.db.entity.join.UserGroupWithRoles;
import cc.rits.membership.console.iam.infrastructure.db.mapper.base.UserGroupBaseMapper;

public interface UserGroupMapper extends UserGroupBaseMapper {

    List<UserGroupWithRoles> selectAll();

    Optional<UserGroupWithRoles> selectById(final Integer userGroupId);

}
