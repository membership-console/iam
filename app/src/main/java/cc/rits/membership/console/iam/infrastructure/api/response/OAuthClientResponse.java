package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuthクライアントレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthClientResponse {

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

}
