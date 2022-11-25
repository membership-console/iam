package cc.rits.membership.console.iam.infrastructure.api.request;

import java.time.LocalDateTime;
import java.util.List;

import cc.rits.membership.console.iam.exception.BadRequestException;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.util.AuthUtil;
import cc.rits.membership.console.iam.util.ValidationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * ユーザ作成リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest implements BaseRequest {

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
     * パスワード
     */
    @Schema(required = true)
    String password;

    /**
     * 入学年度
     */
    @Schema(required = true)
    Integer entranceYear;

    /**
     * ユーザグループIDリスト
     */
    @Singular
    @Schema(required = true)
    List<Integer> userGroupIds;

    /**
     * バリデーション
     */
    @Override
    public void validate() {
        // ファーストネーム
        if (!ValidationUtil.checkStringLength(this.getFirstName(), 1, 255)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_FIRST_NAME);
        }

        // ラストネーム
        if (!ValidationUtil.checkStringLength(this.getLastName(), 1, 255)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_LAST_NAME);
        }

        // メールアドレス
        if (!AuthUtil.isEmailValid(this.getEmail())) {
            throw new BadRequestException(ErrorCode.INVALID_USER_EMAIL);
        }

        // パスワード
        AuthUtil.checkIsPasswordValid(this.getPassword());

        // 入学年度
        final var currentYear = LocalDateTime.now().getYear();
        if (this.getEntranceYear() > currentYear) {
            throw new BadRequestException(ErrorCode.INVALID_USER_ENTRANCE_YEAR);
        }
    }

}
