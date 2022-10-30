package cc.rits.membership.console.iam.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Repository;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.repository.ClientRepository;
import cc.rits.membership.console.iam.enums.Scope;
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

    private final JdbcRegisteredClientRepository registeredClientRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ClientModel> selectAll() {
        final var example = new Oauth2RegisteredClientExample();
        return this.oAuth2RegisteredClientMapper.selectByExample(example).stream() //
            .map(ClientModel::new) //
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientModel> selectById(final String id) {
        return Optional.ofNullable(this.oAuth2RegisteredClientMapper.selectByPrimaryKey(id)) //
            .map(ClientModel::new);
    }

    @Override
    public void insert(final ClientModel clientModel) {
        final var registeredClientBuilder = RegisteredClient.withId(clientModel.getId());
        registeredClientBuilder.clientName(clientModel.getName());
        registeredClientBuilder.clientId(clientModel.getClientId());
        registeredClientBuilder.clientSecret(this.passwordEncoder.encode(clientModel.getClientSecret()));
        registeredClientBuilder.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
        clientModel.getScopes().forEach(scope -> registeredClientBuilder.scope(scope.getName()));
        this.registeredClientRepository.save(registeredClientBuilder.build());
    }

    @Override
    public void updateNameAndScopes(final ClientModel clientModel) {
        final var scopes = clientModel.getScopes().stream() //
            .map(Scope::getName) //
            .collect(Collectors.toList());
        this.oAuth2RegisteredClientMapper.updateClientNameAndScopesById(clientModel.getId(), clientModel.getName(),
            String.join(",", scopes));
    }

    @Override
    public void updateClientIdAndSecret(final ClientModel clientModel) {
        this.oAuth2RegisteredClientMapper.updateClientIdAndSecretById(clientModel.getId(), clientModel.getClientId(),
            this.passwordEncoder.encode(clientModel.getClientSecret()));
    }

    @Override
    public boolean existsByName(final String name) {
        final var example = new Oauth2RegisteredClientExample();
        example.createCriteria().andClientNameEqualTo(name);
        return this.oAuth2RegisteredClientMapper.countByExample(example) != 0;
    }

    @Override
    public boolean existsById(final String id) {
        final var example = new Oauth2RegisteredClientExample();
        example.createCriteria().andIdEqualTo(id);
        return this.oAuth2RegisteredClientMapper.countByExample(example) != 0;
    }

    @Override
    public void deleteById(final String id) {
        this.oAuth2RegisteredClientMapper.deleteByPrimaryKey(id);
    }

}
