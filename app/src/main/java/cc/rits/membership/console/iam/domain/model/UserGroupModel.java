package cc.rits.membership.console.iam.domain.model;

import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRole;
import cc.rits.membership.console.iam.infrastructure.db.entity.join.UserGroupWithRoles;
import lombok.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ユーザグループモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupModel {

    /**
     * ユーザグループID
     */
    Integer id;

    /**
     * ユーザグループ名
     */
    String name;

    /**
     * ロールリスト
     */
    @Singular
    List<Role> roles;

    public UserGroupModel(final UserGroupWithRoles userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
        this.roles = userGroup.getRoles().stream() //
            .map(UserGroupRole::getRoleId) //
            .map(Role::find) //
            .filter(Optional::isPresent) //
            .map(Optional::get) //
            .collect(Collectors.toList());
    }

}
