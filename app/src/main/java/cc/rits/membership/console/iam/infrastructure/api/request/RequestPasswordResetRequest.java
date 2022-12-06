package cc.rits.membership.console.iam.infrastructure.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * パスワードリセット要求リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPasswordResetRequest implements BaseRequest {

    /**
     * メールアドレス
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String email;

    /**
     * バリデーション
     */
    public void validate() {}

}
