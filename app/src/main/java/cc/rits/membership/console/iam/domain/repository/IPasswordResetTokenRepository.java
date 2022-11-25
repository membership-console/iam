package cc.rits.membership.console.iam.domain.repository;

import java.util.Optional;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;

/**
 * パスワードリセットトークンリポジトリ
 */
public interface IPasswordResetTokenRepository {

    /**
     * パスワードリセットトークンを作成
     * 
     * @param passwordResetTokenModel パスワードリセットトークン
     */
    void insert(final PasswordResetTokenModel passwordResetTokenModel);

    /**
     * トークンからパスワードリセットトークンを取得
     * 
     * @param token トークン
     * @return パスワードリセットトークン
     */
    Optional<PasswordResetTokenModel> selectByToken(final String token);

    /**
     * IDからパスワードリセットトークンを削除
     *
     * @param id パスワードリセットトークンID
     */
    void deleteById(final Integer id);

}
