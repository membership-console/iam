package cc.rits.membership.console.iam.helper

import java.time.LocalDate

/**
 * 日付ヘルパー
 */
class DateHelper {

    /**
     * 日付を作成
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 日付
     */
    static LocalDate build(final Integer year, final Integer month, final Integer day) {
        return LocalDate.of(year, month, day)
    }

    /**
     * 明日の日付を取得
     *
     * @return 明日の日付
     */
    static LocalDate tomorrow() {
        return LocalDate.now().plusDays(1)
    }

    /**
     * 今日の日付を取得
     *
     * @return 今日の日付
     */
    static LocalDate today() {
        return LocalDate.now()
    }

    /**
     * 昨日の日付を取得
     *
     * @return 昨日の日付
     */
    static LocalDate yesterday() {
        return LocalDate.now().minusDays(1)
    }

}
