package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.repository.UserGroupRepository;
import cc.rits.membership.console.iam.infrastructure.db.mapper.UserGroupMapper;
import lombok.RequiredArgsConstructor;

/**
 * ユーザグループリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserGroupRepositoryImpl implements UserGroupRepository {

    private final UserGroupMapper userGroupMapper;

    @Override
    public List<UserGroupModel> selectAll() {
        return this.userGroupMapper.selectAll().stream().map(UserGroupModel::new).collect(Collectors.toList());
    }

}
