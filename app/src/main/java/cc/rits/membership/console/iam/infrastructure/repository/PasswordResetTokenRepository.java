package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.PasswordResetTokenModel;
import cc.rits.membership.console.iam.domain.repository.IPasswordResetTokenRepository;
import cc.rits.membership.console.iam.infrastructure.db.entity.PasswordResetTokenExample;
import cc.rits.membership.console.iam.infrastructure.db.mapper.PasswordResetTokenMapper;
import cc.rits.membership.console.iam.infrastructure.factory.PasswordResetTokenFactory;
import lombok.RequiredArgsConstructor;

/**
 * パスワードリセットトークンリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class PasswordResetTokenRepository implements IPasswordResetTokenRepository {

    private final PasswordResetTokenMapper passwordResetTokenMapper;

    private final PasswordResetTokenFactory passwordResetTokenFactory;

    @Override
    public void insert(final PasswordResetTokenModel passwordResetTokenModel) {
        final var passwordResetToken = this.passwordResetTokenFactory.createPasswordResetToken(passwordResetTokenModel);
        this.passwordResetTokenMapper.insert(passwordResetToken);
    }

    @Override
    public Optional<PasswordResetTokenModel> selectByToken(final String token) {
        final var example = new PasswordResetTokenExample();
        example.createCriteria().andTokenEqualTo(token);
        return this.passwordResetTokenMapper.selectByExample(example).stream() //
            .map(PasswordResetTokenModel::new) //
            .findFirst();
    }

    @Override
    public void deleteById(final Integer id) {
        this.passwordResetTokenMapper.deleteByPrimaryKey(id);
    }

}
