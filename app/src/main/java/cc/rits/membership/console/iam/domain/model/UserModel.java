package cc.rits.membership.console.iam.domain.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cc.rits.membership.console.iam.annotation.SwaggerHiddenParameter;
import cc.rits.membership.console.iam.enums.Role;
import cc.rits.membership.console.iam.infrastructure.db.entity.join.UserWithUserGroups;
import lombok.*;

/**
 * ユーザモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SwaggerHiddenParameter
public class UserModel implements Serializable {

    /**
     * ユーザID
     */
    Integer id;

    /**
     * ファーストネーム
     */
    String firstName;

    /**
     * ラストネーム
     */
    String lastName;

    /**
     * メールアドレス
     */
    String email;

    /**
     * パスワード
     */
    String password;

    /**
     * 入学年度
     */
    Integer entranceYear;

    /**
     * 所属するユーザグループリスト
     */
    @Singular
    List<UserGroupModel> userGroups;

    public UserModel(final UserWithUserGroups user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.entranceYear = user.getEntranceYear();
        this.userGroups = user.getUserGroups().stream() //
            .map(UserGroupModel::new) //
            .collect(Collectors.toList());
    }

    /**
     * ロールを持つかチェック
     *
     * @param role ロール
     * @return チェック結果
     */
    public boolean hasRole(final Role role) {
        return this.getUserGroups().stream() //
            .map(UserGroupModel::getRoles) //
            .flatMap(Collection::stream) //
            .anyMatch(r -> r.canImpersonateOtherRole(role));
    }

}
