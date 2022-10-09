package cc.rits.membership.console.iam.infrastructure.factory;

import org.springframework.stereotype.Component;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.db.entity.User;

/**
 * ユーザファクトリ
 */
@Component
public class UserFactory {

    /**
     * Userを作成
     * 
     * @param userModel model
     * @return entity
     */
    public User createUser(final UserModel userModel) {
        return User.builder() //
            .id(userModel.getId()) //
            .firstName(userModel.getFirstName()) //
            .lastName(userModel.getLastName()) //
            .email(userModel.getEmail()) //
            .password(userModel.getPassword()) //
            .entranceYear(userModel.getEntranceYear()) //
            .build();
    }

}
