package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.response.UserResponse
import org.springframework.http.HttpStatus

/**
 * UserRestControllerの統合テスト
 */
class UserRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/users"
    static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me"

    def "ログインユーザ取得API: 正常系 ログインユーザを取得する"() {
        given:
        final user = this.login()

        // @formatter:off
        TableHelper.insert sql, "user_group", {
            id | name
            1  | "グループA"
            2  | "グループB"
        }
        TableHelper.insert sql, "user_group_role", {
            user_group_id | role_id
            1             | Role.IAM_ADMIN.id
            1             | Role.PURCHASE_REQUEST_ADMIN.id
            2             | Role.PURCHASE_REQUEST_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
            user.id | 2
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_LOGIN_USER_PATH)
        final response = this.execute(request, HttpStatus.OK, UserResponse)

        then:
        response.id == user.id
        response.firstName == user.firstName
        response.lastName == user.lastName
        response.email == user.email
        response.entranceYear == user.entranceYear
        response.userGroups*.id == [1, 2]
        response.userGroups*.roles == [[Role.IAM_ADMIN.id, Role.PURCHASE_REQUEST_ADMIN.id], [Role.PURCHASE_REQUEST_ADMIN.id]]
    }

    def "ログインユーザ取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_LOGIN_USER_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
