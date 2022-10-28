package cc.rits.membership.console.iam.domain.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import cc.rits.membership.console.iam.annotation.SwaggerHiddenParameter;
import cc.rits.membership.console.iam.enums.Scope;
import cc.rits.membership.console.iam.infrastructure.db.entity.Oauth2RegisteredClient;
import lombok.*;

/**
 * クライアントモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SwaggerHiddenParameter
public class ClientModel implements Serializable {

    /**
     * ID
     */
    @Builder.Default
    String id = UUID.randomUUID().toString();

    /**
     * クライアント名
     */
    String name;

    /**
     * クライアントID
     */
    String clientId;

    /**
     * クライアントシークレット
     */
    String clientSecret;

    /**
     * スコープリスト
     */
    @Singular
    List<Scope> scopes;

    public ClientModel(final Oauth2RegisteredClient oauth2RegisteredClient) {
        this.id = oauth2RegisteredClient.getId();
        this.name = oauth2RegisteredClient.getClientName();
        this.clientId = oauth2RegisteredClient.getClientId();
        this.clientSecret = oauth2RegisteredClient.getClientSecret();
        this.scopes = Arrays.stream(oauth2RegisteredClient.getScopes().split(",")) //
            .map(Scope::find) //
            .filter(Optional::isPresent) //
            .map(Optional::get) //
            .collect(Collectors.toList());
    }

}
