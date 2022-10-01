package cc.rits.membership.console.iam.infrastructure.repository;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.infrastructure.db.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ユーザリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public Optional<UserModel> selectByEmail(String email) {
        return this.userMapper.selectByEmail(email).map(UserModel::new);
    }

}
