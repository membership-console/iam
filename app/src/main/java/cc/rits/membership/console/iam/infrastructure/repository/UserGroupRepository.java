package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.repository.IUserGroupRepository;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupExample;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRoleExample;
import cc.rits.membership.console.iam.infrastructure.db.mapper.UserGroupMapper;
import cc.rits.membership.console.iam.infrastructure.db.mapper.UserGroupRoleMapper;
import cc.rits.membership.console.iam.infrastructure.factory.UserGroupFactory;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserGroupRepository implements IUserGroupRepository {

    private final UserGroupMapper userGroupMapper;

    private final UserGroupRoleMapper userGroupRoleMapper;

    private final UserGroupFactory userGroupFactory;

    @Override
    public List<UserGroupModel> selectAll() {
        return this.userGroupMapper.selectAll().stream().map(UserGroupModel::new).toList();
    }

    @Override
    public Optional<UserGroupModel> selectById(final Integer id) {
        return this.userGroupMapper.selectById(id).map(UserGroupModel::new);
    }

    @Override
    public List<UserGroupModel> selectByIds(final List<Integer> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        return this.userGroupMapper.selectByIds(ids).stream() //
            .map(UserGroupModel::new) //
            .toList();
    }

    @Override
    public void insert(final UserGroupModel userGroupModel) {
        final var userGroup = this.userGroupFactory.createUserGroup(userGroupModel);
        this.userGroupMapper.insert(userGroup);

        userGroupModel.setId(userGroup.getId());
        final var userGroupRoles = this.userGroupFactory.createUserGroupRoles(userGroupModel);
        if (!userGroupRoles.isEmpty()) {
            this.userGroupRoleMapper.bulkInsert(userGroupRoles);
        }
    }

    @Override
    public boolean existsByName(final String name) {
        final var example = new UserGroupExample();
        example.createCriteria().andNameEqualTo(name);
        return this.userGroupMapper.countByExample(example) != 0;
    }

    @Override
    public boolean existsById(final Integer id) {
        final var example = new UserGroupExample();
        example.createCriteria().andIdEqualTo(id);
        return this.userGroupMapper.countByExample(example) != 0;
    }

    @Override
    public boolean existsByIds(final List<Integer> ids) {
        if (ids.isEmpty()) {
            return true;
        }

        final var example = new UserGroupExample();
        example.createCriteria().andIdIn(ids);
        return this.userGroupMapper.countByExample(example) == ids.size();
    }

    @Override
    public void deleteById(final Integer id) {
        this.userGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(final UserGroupModel userGroupModel) {
        final var userGroup = this.userGroupFactory.createUserGroup(userGroupModel);
        this.userGroupMapper.updateByPrimaryKey(userGroup);

        // 既存のロールリストを削除
        final var userGroupRoleExample = new UserGroupRoleExample();
        userGroupRoleExample.createCriteria().andUserGroupIdEqualTo(userGroupModel.getId());
        this.userGroupRoleMapper.deleteByExample(userGroupRoleExample);

        // 新規ロールリストを追加
        final var userGroupRoles = this.userGroupFactory.createUserGroupRoles(userGroupModel);
        if (!userGroupRoles.isEmpty()) {
            this.userGroupRoleMapper.bulkInsert(userGroupRoles);
        }
    }

}
