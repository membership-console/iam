package cc.rits.membership.console.iam.infrastructure.api.request;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.enums.Scope;
import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.util.ValidationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * クライアント作成/更新リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpsertRequest implements BaseRequest {

    /**
     * クライアント名
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name;

    /**
     * スコープリスト
     */
    @Singular
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<String> scopes;

    /**
     * バリデーション
     */
    public void validate() {
        // ユーザグループ名
        if (!ValidationUtil.checkStringLength(this.getName(), 1, 100)) {
            throw new BadRequestException(ErrorCode.INVALID_CLIENT_NAME);
        }

        // スコープリスト
        if (this.getScopes().isEmpty()) {
            throw new BadRequestException(ErrorCode.CLIENT_SCOPES_MUST_NOT_BE_EMPTY);
        }
        if (this.getScopes().stream().map(Scope::find).anyMatch(Optional::isEmpty)) {
            throw new BadRequestException(ErrorCode.INVALID_CLIENT_SCOPES);
        }
    }

}
