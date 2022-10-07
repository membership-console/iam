package cc.rits.membership.console.iam.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * プロジェクトプロパティ
 */
@Data
@Configuration
@ConfigurationProperties("auth")
public class AuthProperty {

    /**
     * セッションタイムアウト[秒]
     */
    Integer sessionTimeout;

}
