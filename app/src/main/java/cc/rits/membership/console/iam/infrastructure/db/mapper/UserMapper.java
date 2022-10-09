package cc.rits.membership.console.iam.infrastructure.db.mapper;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.infrastructure.db.entity.join.UserWithUserGroups;
import cc.rits.membership.console.iam.infrastructure.db.mapper.base.UserBaseMapper;

public interface UserMapper extends UserBaseMapper {

    Optional<UserWithUserGroups> selectByEmail(final String email);

    Optional<UserWithUserGroups> selectById(final Integer id);

    List<UserWithUserGroups> selectAll();

    long countByUserGroupId(final Integer userGroupId);

    void updatePasswordById(final Integer id, final String password);

    void addUserToUserGroups(final Integer userId, final List<Integer> userGroupIds);

}
