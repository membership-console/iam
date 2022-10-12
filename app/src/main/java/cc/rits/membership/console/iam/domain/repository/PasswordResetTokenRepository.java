package cc.rits.membership.console.iam.domain.repository;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;

/**
 * パスワードリセットトークンリポジトリ
 */
public interface PasswordResetTokenRepository {

    /**
     * パスワードリセットトークンを作成
     * 
     * @param passwordResetTokenModel パスワードリセットトークン
     */
    void insert(final PasswordResetTokenModel passwordResetTokenModel);

}
