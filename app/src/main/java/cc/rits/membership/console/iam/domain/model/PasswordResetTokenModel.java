package cc.rits.membership.console.iam.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import cc.rits.membership.console.iam.annotation.SwaggerHiddenParameter;
import cc.rits.membership.console.iam.infrastructure.db.entity.PasswordResetToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * パスワードリセットトークンモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SwaggerHiddenParameter
public class PasswordResetTokenModel implements Serializable {

    /**
     * パスワードリセットトークンID
     */
    Integer id;

    /**
     * ユーザID
     */
    Integer userId;

    /**
     * トークン
     */
    @Builder.Default
    String token = UUID.randomUUID().toString();

    /**
     * 有効期限 (デフォルトは30分間)
     */
    @Builder.Default
    LocalDateTime expireAt = LocalDateTime.now().plusMinutes(30);

    public PasswordResetTokenModel(final PasswordResetToken passwordResetToken) {
        this.id = passwordResetToken.getId();
        this.userId = passwordResetToken.getUserId();
        this.token = passwordResetToken.getToken();
        this.expireAt = passwordResetToken.getExpireAt();
    }

    /**
     * パスワードリセットトークンが有効かチェック
     * 
     * @return チェック結果
     */
    public boolean isValid() {
        return this.expireAt.isAfter(LocalDateTime.now());
    }

}
