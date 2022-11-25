package cc.rits.membership.console.iam.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ロール
 */
@Getter
@AllArgsConstructor
public enum Role {

    /**
     * IAMの管理者
     */
    IAM_ADMIN(0),

    /**
     * 会計システムの管理者
     */
    PAYMASTER_ADMIN(1),

    /**
     * リマインダーの管理者
     */
    REMINDER_ADMIN(2);

    /**
     * ロールID
     */
    private final Integer id;

    /**
     * ロールを検索
     *
     * @param id ロールID
     * @return ロール
     */
    public static Optional<Role> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst();
    }

}
