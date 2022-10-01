package cc.rits.membership.console.iam.infrastructure.api.request;

import cc.rits.membership.console.iam.exception.BaseException;

/**
 * リクエストボディのインターフェース
 */
public interface BaseRequest {

    /**
     * リクエストボディのバリデーション
     */
    void validate() throws BaseException;

}
