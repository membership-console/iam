package cc.rits.membership.console.iam.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * SendGridプロパティ
 */
@Data
@Configuration
@ConfigurationProperties("sendgrid")
public class SendGridProperty {

    /**
     * API KEY
     */
    String apiKey;

    /**
     * 差出人
     */
    From from;

    /**
     * 有効フラグ
     */
    Boolean enabled;

    @Data
    public static class From {

        /**
         * 名前
         */
        String name;

        /**
         * メールアドレス
         */
        String email;

    }

}
