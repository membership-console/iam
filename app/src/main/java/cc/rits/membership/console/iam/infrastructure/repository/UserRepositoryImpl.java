package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserExample;
import cc.rits.membership.console.iam.infrastructure.db.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

/**
 * ユーザリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public Optional<UserModel> selectByEmail(final String email) {
        return this.userMapper.selectByEmail(email).map(UserModel::new);
    }

    @Override
    public List<UserModel> selectAll() {
        return this.userMapper.selectAll().stream() //
            .map(UserModel::new) //
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(final Integer id) {
        this.userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public boolean existsById(final Integer id) {
        final var example = new UserExample();
        example.createCriteria().andIdEqualTo(id);
        return this.userMapper.countByExample(example) != 0;
    }

}
