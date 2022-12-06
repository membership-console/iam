package cc.rits.membership.console.iam.infrastructure.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroup;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRole;

/**
 * ユーザグループファクトリ
 */
@Component
public class UserGroupFactory {

    /**
     * UserGroupを作成
     * 
     * @param userGroupModel model
     * @return entity
     */
    public UserGroup createUserGroup(final UserGroupModel userGroupModel) {
        return UserGroup.builder() //
            .id(userGroupModel.getId()) //
            .name(userGroupModel.getName()) //
            .build();
    }

    /**
     * UserGroupRoleを作成
     *
     * @param userGroupModel model
     * @return entities
     */
    public List<UserGroupRole> createUserGroupRoles(final UserGroupModel userGroupModel) {
        return userGroupModel.getRoles().stream() //
            .map(Role::getId) //
            .map(roleId -> new UserGroupRole(userGroupModel.getId(), roleId)) //
            .toList();
    }

}
