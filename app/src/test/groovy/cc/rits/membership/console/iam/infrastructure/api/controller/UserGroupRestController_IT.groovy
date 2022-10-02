package cc.rits.membership.console.iam.infrastructure.api.controller

import cc.rits.membership.console.iam.enums.Role
import cc.rits.membership.console.iam.exception.ErrorCode
import cc.rits.membership.console.iam.exception.ForbiddenException
import cc.rits.membership.console.iam.exception.UnauthorizedException
import cc.rits.membership.console.iam.helper.TableHelper
import cc.rits.membership.console.iam.infrastructure.api.response.UserGroupsResponse
import org.springframework.http.HttpStatus

/**
 * UserGroupRestControllerの統合テスト
 */
class UserGroupRestController_IT extends AbstractRestController_IT {

    // API PATH
    static final String BASE_PATH = "/api/user-groups"
    static final String GET_USER_GROUPS_PATH = BASE_PATH

    def "ユーザグループリスト取得API: 正常系 IAMの閲覧者がユーザグループリストを取得"() {
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
            1             | role.id
            2             | Role.PURCHASE_REQUEST_ADMIN.id
        }
        TableHelper.insert sql, "r__user__user_group", {
            user_id | user_group_id
            user.id | 1
        }
        // @formatter:on

        when:
        final request = this.getRequest(GET_USER_GROUPS_PATH)
        final response = this.execute(request, HttpStatus.OK, UserGroupsResponse)

        then:
        response.userGroups*.id == [1, 2]
        response.userGroups*.roles == [[role.id], [Role.PURCHASE_REQUEST_ADMIN.id]]

        where:
        role << [Role.IAM_VIEWER, Role.IAM_ADMIN]
    }

    def "ユーザグループリスト取得API: 異常系 IAMの閲覧者以外は403エラー"() {
        given:
        this.login()

        expect:
        final request = this.getRequest(GET_USER_GROUPS_PATH)
        this.execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION))
    }

    def "ユーザグループリスト取得API: 異常系 ログインしていない場合は401エラー"() {
        expect:
        final request = this.getRequest(GET_USER_GROUPS_PATH)
        this.execute(request, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
