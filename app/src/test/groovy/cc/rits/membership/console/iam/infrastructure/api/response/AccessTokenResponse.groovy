package cc.rits.membership.console.iam.infrastructure.api.response

import lombok.Data

/**
 * アクセストークンレスポンス
 */
@Data
class AccessTokenResponse {

    String access_token

    String token_type

    String scope

    Integer expires_in

}
