package cc.rits.membership.console.iam.infrastructure.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * エラーレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * エラーメッセージ
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String message;

    /**
     * エラーコード
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer code;

}
