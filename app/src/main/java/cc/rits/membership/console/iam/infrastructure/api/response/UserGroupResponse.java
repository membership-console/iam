package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザグループレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupResponse {

    /**
     * ユーザグループID
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer id;

    /**
     * ユーザグループ名
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name;

    /**
     * ロールリスト
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<Integer> roles;

    public UserGroupResponse(final UserGroupModel userGroupModel) {
        this.id = userGroupModel.getId();
        this.name = userGroupModel.getName();
        this.roles = userGroupModel.getRoles().stream().map(Role::getId).toList();
    }

}
