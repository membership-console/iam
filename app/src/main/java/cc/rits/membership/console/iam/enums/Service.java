package cc.rits.membership.console.iam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 連携サービス
 */
@Getter
@AllArgsConstructor
public enum Service {

    /**
     * IAM
     */
    IAM(0),

    /**
     * 会計システム
     */
    PAYMASTER(1);

    /**
     * サービスID
     */
    private final Integer id;

}
