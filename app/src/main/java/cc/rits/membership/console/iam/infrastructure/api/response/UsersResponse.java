package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;

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
    List<UserResponse> users;

}
