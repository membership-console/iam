package cc.rits.membership.console.iam.infrastructure.factory;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;
import cc.rits.membership.console.iam.infrastructure.db.entity.PasswordResetToken;

/**
 * パスワードリセットトークンファクトリ
 */
@Component
public class PasswordResetTokenFactory {

    /**
     * PasswordResetTokenを作成
     * 
     * @param passwordResetToken model
     * @return entity
     */
    public PasswordResetToken createPasswordResetToken(final PasswordResetTokenModel passwordResetToken) {
        return PasswordResetToken.builder() //
            .id(passwordResetToken.getId()) //
            .userId(passwordResetToken.getUserId()) //
            .token(passwordResetToken.getToken()) //
            .expireAt(passwordResetToken.getExpireAt()) //
            .build();
    }

}
