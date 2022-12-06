package cc.rits.membership.console.iam.infrastructure.api.response;

import java.util.Collection;
import java.util.List;

import cc.rits.membership.console.iam.domain.model.UserGroupModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.enums.Role;
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
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer id;

    /**
     * ファーストネーム
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String firstName;

    /**
     * ラストネーム
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String lastName;

    /**
     * メールアドレス
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String email;

    /**
     * 入学年度
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer entranceYear;

    /**
     * ユーザグループリスト
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<UserGroupResponse> userGroups;

    /**
     * ロールリスト
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, enumAsRef = true)
    List<Role> roles;

    public UserResponse(final UserModel userModel) {
        this.id = userModel.getId();
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.email = userModel.getEmail();
        this.entranceYear = userModel.getEntranceYear();
        this.userGroups = userModel.getUserGroups().stream().map(UserGroupResponse::new).toList();
        this.roles = userModel.getUserGroups().stream() //
            .map(UserGroupModel::getRoles) //
            .flatMap(Collection::stream) //
            .distinct() //
            .toList();
    }

}
