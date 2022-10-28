package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.infrastructure.db.entity.Oauth2RegisteredClientExample;
import cc.rits.membership.console.iam.infrastructure.db.mapper.OAuth2RegisteredClientMapper;
import lombok.RequiredArgsConstructor;

/**
 * クライアントリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final OAuth2RegisteredClientMapper oAuth2RegisteredClientMapper;

    @Override
    public List<ClientModel> selectAll() {
        // TODO: UTを書く
        final var example = new Oauth2RegisteredClientExample();
        return this.oAuth2RegisteredClientMapper.selectByExample(example).stream() //
            .map(ClientModel::new) //
            .collect(Collectors.toList());
    }

}
