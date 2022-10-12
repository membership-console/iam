package cc.rits.membership.console.iam.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;
import cc.rits.membership.console.iam.domain.repository.PasswordResetTokenRepository;
import cc.rits.membership.console.iam.infrastructure.db.mapper.PasswordResetTokenMapper;
import cc.rits.membership.console.iam.infrastructure.factory.PasswordResetTokenFactory;
import lombok.RequiredArgsConstructor;

/**
 * パスワードリセットトークンリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final PasswordResetTokenMapper passwordResetTokenMapper;

    private final PasswordResetTokenFactory passwordResetTokenFactory;

    @Override
    public void insert(final PasswordResetTokenModel passwordResetTokenModel) {
        final var passwordResetToken = this.passwordResetTokenFactory.createPasswordResetToken(passwordResetTokenModel);
        this.passwordResetTokenMapper.insert(passwordResetToken);
    }

}
