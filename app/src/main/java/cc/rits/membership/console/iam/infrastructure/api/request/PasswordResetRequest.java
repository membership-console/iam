package cc.rits.membership.console.iam.infrastructure.api.request;

import cc.rits.membership.console.iam.util.AuthUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * パスワードリセットリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest implements BaseRequest {

    /**
     * 新しいパスワード
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String newPassword;

    /**
     * トークン
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String token;

    /**
     * バリデーション
     */
    public void validate() {
        // 新しいパスワード
        AuthUtil.checkIsPasswordValid(this.getNewPassword());
    }

}
