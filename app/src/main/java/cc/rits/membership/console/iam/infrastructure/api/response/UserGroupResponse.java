package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;
import java.util.stream.Collectors;

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
    @Schema(required = true)
    Integer id;

    /**
     * ユーザグループ名
     */
    @Schema(required = true)
    String name;

    /**
     * ロールリスト
     */
    @Schema(required = true)
    List<Integer> roles;

    public UserGroupResponse(final UserGroupModel userGroupModel) {
        this.id = userGroupModel.getId();
        this.name = userGroupModel.getName();
        this.roles = userGroupModel.getRoles().stream().map(Role::getId).collect(Collectors.toList());
    }

}
