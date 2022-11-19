package cc.rits.membership.console.iam

import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.annotation.Transactional

import javax.sql.DataSource

/**
 * DBテストの基底クラス
 */
@Transactional
class AbstractDatabaseSpecification extends AbstractSpecification {

    /**
     * SQL Handler
     */
    protected static Sql sql

    @Autowired
    private dataSource(final DataSource dataSource) {
        if (Objects.isNull(sql)) {
            sql = new Sql(new TransactionAwareDataSourceProxy(dataSource))
        }
    }

    /**
     * cleanup after test case
     */
    def cleanup() {
        // DBを初期化するために、テスト終了時にロールバック
        sql.rollback()
    }

}
