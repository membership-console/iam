package cc.rits.membership.console.iam.infrastructure.api.request;

import java.util.List;

import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * メール送信リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRequest implements BaseRequest {

    /**
     * ユーザIDリスト
     */
    @Schema(required = true)
    List<Integer> userIds;

    /**
     * 件名
     */
    @Schema(required = true)
    String subject;

    /**
     * 本文
     */
    @Schema(required = true)
    String body;

    /**
     * バリデーション
     */
    @Override
    public void validate() {
        if (this.userIds.isEmpty()) {
            throw new BadRequestException(ErrorCode.USER_IDS_MUST_NOT_BE_EMPTY);
        }
    }

}
