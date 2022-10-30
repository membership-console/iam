package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.UserRepository;
import cc.rits.membership.console.iam.infrastructure.db.entity.User;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserExample;
import cc.rits.membership.console.iam.infrastructure.db.mapper.UserMapper;
import cc.rits.membership.console.iam.infrastructure.factory.UserFactory;
import lombok.RequiredArgsConstructor;

/**
 * ユーザリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    private final UserFactory userFactory;

    @Override
    public Optional<UserModel> selectByEmail(final String email) {
        return this.userMapper.selectByEmail(email).map(UserModel::new);
    }

    @Override
    public Optional<UserModel> selectById(final Integer id) {
        return this.userMapper.selectById(id).map(UserModel::new);
    }

    @Override
    public List<String> selectEmailsByIds(final List<Integer> ids) {
        final var example = new UserExample();
        example.createCriteria().andIdIn(ids);
        return this.userMapper.selectByExample(example).stream() //
            .map(User::getEmail) //
            .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> selectAll() {
        return this.userMapper.selectAll().stream() //
            .map(UserModel::new) //
            .collect(Collectors.toList());
    }

    @Override
    public void insert(final UserModel userModel) {
        final var user = this.userFactory.createUser(userModel);
        this.userMapper.insert(user);

        // ユーザグループに配属
        final var userGroupIds = userModel.getUserGroups().stream() //
            .map(UserGroupModel::getId) //
            .collect(Collectors.toList());
        this.userMapper.addUserToUserGroups(user.getId(), userGroupIds);
    }

    @Override
    public void update(final UserModel userModel) {
        final var user = this.userFactory.createUser(userModel);
        this.userMapper.updateByPrimaryKey(user);

        // 既存のユーザグループ配属情報を削除し、新しいユーザグループリストに再配属する
        this.userMapper.deleteUserToUserGroups(user.getId());
        final var userGroupIds = userModel.getUserGroups().stream() //
            .map(UserGroupModel::getId) //
            .collect(Collectors.toList());
        this.userMapper.addUserToUserGroups(user.getId(), userGroupIds);
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

    @Override
    public boolean existsByEmail(final String email) {
        final var example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        return this.userMapper.countByExample(example) != 0;
    }

    @Override
    public Integer countByUserGroupId(final Integer userGroupId) {
        return Math.toIntExact(this.userMapper.countByUserGroupId(userGroupId));
    }

    @Override
    public void updatePasswordById(final Integer id, final String password) {
        this.userMapper.updatePasswordById(id, password);
    }

}
