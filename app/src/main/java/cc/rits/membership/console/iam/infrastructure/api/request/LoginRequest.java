package cc.rits.membership.console.iam.infrastructure.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログインリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements BaseRequest {

    /**
     * メールアドレス
     */
    @Schema(required = true)
    String email;

    /**
     * パスワード
     */
    @Schema(required = true)
    String password;

    /**
     * バリデーション
     */
    public void validate() {
    }

}
