package cc.rits.membership.console.iam.infrastructure.api.request;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.util.ValidationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * ユーザグループ作成/更新リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupUpsertRequest implements BaseRequest {

    /**
     * ユーザグループ名
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name;

    /**
     * ロールリスト
     */
    @Singular
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<Integer> roles;

    /**
     * バリデーション
     */
    public void validate() {
        // ユーザグループ名
        if (!ValidationUtil.checkStringLength(this.getName(), 1, 100)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_GROUP_NAME);
        }

        // ロールリスト
        if (this.getRoles().isEmpty()) {
            throw new BadRequestException(ErrorCode.USER_GROUP_ROLES_MUST_NOT_BE_EMPTY);
        }
        if (this.getRoles().stream().map(Role::find).anyMatch(Optional::isEmpty)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_GROUP_ROLES);
        }
    }

}
