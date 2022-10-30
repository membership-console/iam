package cc.rits.membership.console.iam.infrastructure.db.mapper;

import cc.rits.membership.console.iam.infrastructure.db.mapper.base.Oauth2RegisteredClientBaseMapper;

public interface OAuth2RegisteredClientMapper extends Oauth2RegisteredClientBaseMapper {

    void updateClientNameAndScopesById(final String id, final String clientName, final String scopes);

    void updateClientIdAndSecretById(final String id, final String clientId, final String clientSecret);

}
