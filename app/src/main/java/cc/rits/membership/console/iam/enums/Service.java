package cc.rits.membership.console.iam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * サービス
 */
@Getter
@AllArgsConstructor
public enum Service {

    /**
     * IAM
     */
    IAM(0),

    /**
     * 購入申請
     */
    PURCHASE_REQUEST(1);

    /**
     * サービスID
     */
    private final Integer id;

}
