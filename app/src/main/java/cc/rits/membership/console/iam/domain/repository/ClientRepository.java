package cc.rits.membership.console.iam.domain.repository;

import java.util.List;
import java.util.Optional;

import cc.rits.membership.console.iam.domain.model.ClientModel;

/**
 * クライアントリポジトリ
 */
public interface ClientRepository {

    /**
     * クライアントリストを全件取得
     * 
     * @return クライアントリスト
     */
    List<ClientModel> selectAll();

    /**
     * IDからクライアントを取得
     * 
     * @param id ID
     * @return クライアント
     */
    Optional<ClientModel> selectById(final String id);

    /**
     * クライアントを作成
     *
     * @param clientModel クライアント
     */
    void insert(final ClientModel clientModel);

    /**
     * クライアント名の存在確認
     *
     * @param name クライアント名
     * @return クライアントが存在するか
     */
    boolean existsByName(final String name);

    /**
     * IDの存在確認
     *
     * @param id ID
     * @return クライアントが存在するか
     */
    boolean existsById(final String id);

    /**
     * IDからクライアントを削除
     * 
     * @param id ID
     */
    void deleteById(final String id);

}
