package cc.rits.membership.console.iam.infrastructure.db.mapper;

import cc.rits.membership.console.iam.infrastructure.db.entity.join.UserWithUserGroups;
import cc.rits.membership.console.iam.infrastructure.db.mapper.base.UserBaseMapper;

import java.util.Optional;

public interface UserMapper extends UserBaseMapper {

    Optional<UserWithUserGroups> selectByEmail(final String email);

}