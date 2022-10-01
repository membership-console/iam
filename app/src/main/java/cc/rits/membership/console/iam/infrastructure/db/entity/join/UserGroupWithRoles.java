package cc.rits.membership.console.iam.infrastructure.db.entity.join;

import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroup;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ユーザグループ + ロールリスト
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGroupWithRoles extends UserGroup {

    /**
     * ロールリスト
     */
    List<UserGroupRole> roles;

}
