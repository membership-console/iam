package cc.rits.membership.console.iam.domain.model;

import java.io.Serializable;
import java.util.*;

import org.springframework.security.crypto.keygen.KeyGenerators;

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
    @Builder.Default
    String clientId = Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey());

    /**
     * クライアントシークレット
     */
    @Builder.Default
    String clientSecret = Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey());

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
            .toList();
    }

    /**
     * スコープを持つかチェック
     *
     * @param scope スコープ
     * @return チェック結果
     */
    public boolean hasScope(final Scope scope) {
        return this.getScopes().stream().anyMatch(scope::equals);
    }

}
