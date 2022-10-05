package cc.rits.membership.console.iam.infrastructure.db.mapper;

import java.util.List;

import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRole;
import cc.rits.membership.console.iam.infrastructure.db.mapper.base.UserGroupRoleBaseMapper;

public interface UserGroupRoleMapper extends UserGroupRoleBaseMapper {

    void bulkInsert(final List<UserGroupRole> userGroupRoles);

}
