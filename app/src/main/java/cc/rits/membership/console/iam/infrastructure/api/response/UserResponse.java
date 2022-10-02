package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.List;
import java.util.stream.Collectors;

import cc.rits.membership.console.iam.domain.model.UserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * ユーザID
     */
    @Schema(required = true)
    Integer id;

    /**
     * ファーストネーム
     */
    @Schema(required = true)
    String firstName;

    /**
     * ラストネーム
     */
    @Schema(required = true)
    String lastName;

    /**
     * メールアドレス
     */
    @Schema(required = true)
    String email;

    /**
     * 入学年度
     */
    @Schema(required = true)
    Integer entranceYear;

    /**
     * ユーザグループリスト
     */
    @Schema(required = true)
    List<UserGroupResponse> userGroups;

    public UserResponse(final UserModel userModel) {
        this.id = userModel.getId();
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.email = userModel.getEmail();
        this.entranceYear = userModel.getEntranceYear();
        this.userGroups = userModel.getUserGroups().stream().map(UserGroupResponse::new).collect(Collectors.toList());
    }

}
