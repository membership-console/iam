package cc.rits.membership.console.iam.infrastructure.api.request;

import cc.rits.membership.console.iam.util.AuthUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログインユーザパスワード更新リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserPasswordUpdateRequest implements BaseRequest {

    /**
     * 古いパスワード
     */
    @Schema(required = true)
    String oldPassword;

    /**
     * 新しいパスワード
     */
    @Schema(required = true)
    String newPassword;

    /**
     * バリデーション
     */
    public void validate() {
        // 新しいパスワード
        AuthUtil.checkIsPasswordValid(this.getNewPassword());
    }

}
