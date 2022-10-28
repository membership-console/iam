package cc.rits.membership.console.iam.domain.repository;

import java.util.List;

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

}
