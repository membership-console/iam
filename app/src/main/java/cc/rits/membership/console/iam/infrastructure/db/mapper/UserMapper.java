package cc.rits.membership.console.iam.infrastructure.db.mapper;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.infrastructure.db.entity.join.UserWithUserGroups;
import cc.rits.membership.console.iam.infrastructure.db.mapper.base.UserBaseMapper;

public interface UserMapper extends UserBaseMapper {

    Optional<UserWithUserGroups> selectByEmail(final String email);

    List<UserWithUserGroups> selectAll();

}
