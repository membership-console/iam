package cc.rits.membership.console.iam.infrastructure.api.response;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * クライアント認証情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientCredentialsResponse {

    /**
     * クライアントID
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String clientId;

    /**
     * クライアントシークレット
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String clientSecret;

    public ClientCredentialsResponse(final ClientModel clientModel) {
        this.clientId = clientModel.getClientId();
        this.clientSecret = clientModel.getClientSecret();
    }

}
