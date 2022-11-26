package cc.rits.membership.console.iam

import cc.rits.membership.console.iam.exception.BaseException
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.lang.Specification

import javax.sql.DataSource

/**
 * テストの基底クラス
 */
@SpringBootTest
abstract class AbstractSpecification extends Specification {

    /**
     * SQL Handler
     */
    @Autowired
    Sql sql

    /**
     * 例外を検証
     *
     * @param thrownException 発生した例外
     * @param expectedException 期待する例外
     */
    static void verifyException(final BaseException thrownException, final BaseException expectedException) {
        assert thrownException.httpStatus == expectedException.httpStatus
        assert thrownException.errorCode == expectedException.errorCode
    }

    @Configuration
    static class TestConfig {

        @Bean
        Sql sql(final DataSource dataSource) {
            return new Sql(dataSource)
        }

    }

}
