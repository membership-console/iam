package cc.rits.membership.console.iam.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisの設定
 */
@MapperScan("cc.rits.membership.console.iam.infrastructure.db.mapper")
@Configuration
public class MyBatisConfig {
}
