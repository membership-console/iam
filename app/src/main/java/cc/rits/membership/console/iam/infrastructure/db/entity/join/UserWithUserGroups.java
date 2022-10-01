package cc.rits.membership.console.iam.infrastructure.db.entity.join;

import cc.rits.membership.console.iam.infrastructure.db.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ユーザ + ユーザグループリスト
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserWithUserGroups extends User {

    /**
     * ユーザグループリスト
     */
    List<UserGroupWithRoles> userGroups;

}
