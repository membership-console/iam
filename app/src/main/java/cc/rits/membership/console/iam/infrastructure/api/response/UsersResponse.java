package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザリストレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

    /**
     * ユーザリスト
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<UserResponse> users;

}
