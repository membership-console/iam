package cc.rits.membership.console.iam.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ロール
 */
@Getter
@AllArgsConstructor
public enum Role {

    /**
     * IAMの閲覧者
     */
    IAM_VIEWER(0, Service.IAM, RoleLevel.VIEWER),

    /**
     * IAMの管理者
     */
    IAM_ADMIN(1, Service.IAM, RoleLevel.ADMIN),

    /**
     * 購入申請の閲覧者
     */
    PURCHASE_REQUEST_VIEWER(2, Service.PURCHASE_REQUEST, RoleLevel.VIEWER),

    /**
     * 購入申請の管理者
     */
    PURCHASE_REQUEST_ADMIN(3, Service.PURCHASE_REQUEST, RoleLevel.ADMIN);

    /**
     * ロールID
     */
    private final Integer id;

    private final Service service;

    private final RoleLevel roleLevel;

    /**
     * ロールを検索
     *
     * @param id ロールID
     * @return ロール
     */
    public static Optional<Role> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst();
    }

    /**
     * 別ロールの動作が許可されているかチェック
     *
     * 例: 「IAMの管理者」は「IAMの閲覧者」になりすませる
     *
     * @param role ロール
     * @return 別ロールの動作が許可されているか
     */
    public Boolean canImpersonateOtherRole(final Role role) {
        return this.service.equals(role.getService()) && this.roleLevel.isComprised(role.getRoleLevel());
    }

    /**
     * ロールレベル
     *
     * 「XXXのYYY (例: IAMの閲覧者)」の「YYY」のレベル
     */
    @Getter
    @AllArgsConstructor
    private enum RoleLevel {

        /**
         * 閲覧者
         */
        VIEWER(0),

        /**
         * 管理者
         */
        ADMIN(1);

        /**
         * ロールタイプレベル
         *
         * レベルが高いほど、権限も強くなる
         */
        private final Integer level;

        /**
         * ロールレベルが別ロールレベルの権限を含むかチェック
         *
         * 例: 「管理者」は「閲覧者」の権限も含む
         *
         * @param roleLevel ロールタイプ
         * @return ロールレベルが別ロールレベルの権限を含むか
         */
        public Boolean isComprised(final RoleLevel roleLevel) {
            return this.getLevel() >= roleLevel.getLevel();
        }
    }

}
