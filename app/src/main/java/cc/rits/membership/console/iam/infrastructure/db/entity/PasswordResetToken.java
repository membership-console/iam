package cc.rits.membership.console.iam.infrastructure.db.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column password_reset_token.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column password_reset_token.user_id
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column password_reset_token.token
     *
     * @mbg.generated
     */
    private String token;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column password_reset_token.expire_at
     *
     * @mbg.generated
     */
    private LocalDateTime expireAt;
}