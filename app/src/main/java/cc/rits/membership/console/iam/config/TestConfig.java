package cc.rits.membership.console.iam.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import lombok.RequiredArgsConstructor;

/**
 * テストの設定
 */
@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // テスト用DBをマイグレーションするのは面倒なので、テスト開始前に初期化する
            flyway.clean();
            flyway.migrate();
        };
    }

    @Bean
    public DataSource dataSource() {
        // アプリケーション/テストで実行されるトランザクションを1つにまとめる
        return new TransactionAwareDataSourceProxy( //
            DataSourceBuilder.create() //
                .username(this.dataSourceProperties.getUsername()) //
                .password(this.dataSourceProperties.getPassword()) //
                .url(this.dataSourceProperties.getUrl()) //
                .driverClassName(this.dataSourceProperties.getDriverClassName()) //
                .build() //
        );
    }

}
