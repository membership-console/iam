package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;
import java.util.stream.Collectors;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.enums.Scope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * クライアントレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {

    /**
     * ID
     */
    @Schema(required = true)
    String id;

    /**
     * クライアント名
     */
    @Schema(required = true)
    String name;

    /**
     * クライアントID
     */
    @Schema(required = true)
    String clientId;

    /**
     * スコープリスト
     */
    @Schema(required = true)
    List<String> scopes;

    public ClientResponse(final ClientModel clientModel) {
        this.id = clientModel.getId();
        this.name = clientModel.getName();
        this.clientId = clientModel.getClientId();
        this.scopes = clientModel.getScopes().stream() //
            .map(Scope::getName) //
            .collect(Collectors.toList());;
    }

}
