package cc.rits.membership.console.iam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

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
     * 購入申請の管理者
     */
    PURCHASE_REQUEST_ADMIN(1);

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
