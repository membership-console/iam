package cc.rits.membership.console.iam.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OAuthクライアントのスコープ
 */
@Getter
@AllArgsConstructor
public enum Scope {

    /**
     * ユーザ情報の読み取り権限
     */
    USER_READ("user:read"),

    /**
     * メールの送信権限
     */
    EMAIL("email");

    /**
     * スコープ名
     */
    private final String name;

    /**
     * スコープを検索
     *
     * @param name スコープ名
     * @return スコープ
     */
    public static Optional<Scope> find(final String name) {
        return Arrays.stream(values()).filter(e -> e.getName().equals(name)).findFirst();
    }

    public String getAuthorityName() {
        return "SCOPE_" + this.getName();
    }

}
