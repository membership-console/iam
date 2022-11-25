package cc.rits.membership.console.iam.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * エラーコード
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 20000~20999: 400 Bad Request
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 20001, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, 20002, "exception.bad_request.invalid_request_parameter"),

    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, 20003, "exception.bad_request.invalid_password_length"),

    PASSWORD_IS_TOO_SIMPLE(HttpStatus.BAD_REQUEST, 20004, "exception.bad_request.password_is_too_simple"),

    INVALID_USER_GROUP_NAME(HttpStatus.BAD_REQUEST, 20005, "exception.bad_request.invalid_user_group_name"),

    INVALID_USER_GROUP_ROLES(HttpStatus.BAD_REQUEST, 20006, "exception.bad_request.invalid_user_group_roles"),

    USER_GROUP_ROLES_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, 20007, "exception.bad_request.user_group_roles_must_not_be_empty"),

    USER_GROUP_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, 20008, "exception.bad_request.user_group_cannot_be_deleted"),

    INVALID_OLD_PASSWORD(HttpStatus.BAD_REQUEST, 20009, "exception.bad_request.invalid_old_password"),

    INVALID_USER_FIRST_NAME(HttpStatus.BAD_REQUEST, 20010, "exception.bad_request.invalid_user_first_name"),

    INVALID_USER_LAST_NAME(HttpStatus.BAD_REQUEST, 20011, "exception.bad_request.invalid_user_last_name"),

    INVALID_USER_EMAIL(HttpStatus.BAD_REQUEST, 20012, "exception.bad_request.invalid_user_email"),

    INVALID_USER_ENTRANCE_YEAR(HttpStatus.BAD_REQUEST, 20013, "exception.bad_request.invalid_user_entrance_year"),

    REQUESTED_EMAIL_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, 20014, "exception.bad_request.requested_email_is_exists"),

    INVALID_PASSWORD_RESET_TOKEN(HttpStatus.BAD_REQUEST, 20015, "exception.bad_request.invalid_password_reset_token"),

    INVALID_CLIENT_NAME(HttpStatus.BAD_REQUEST, 20016, "exception.bad_request.invalid_client_name"),

    INVALID_CLIENT_SCOPES(HttpStatus.BAD_REQUEST, 20017, "exception.bad_request.invalid_client_scopes"),

    CLIENT_SCOPES_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, 20018, "exception.bad_request.client_scopes_must_not_be_empty"),

    USER_IDS_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, 20019, "exception.bad_request.user_ids_must_not_be_empty"),

    /**
     * 21000~21999: 401 Unauthorized
     */
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, 21000, "exception.unauthorized.user_not_logged_in"),

    INCORRECT_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, 21001, "exception.unauthorized.incorrect_email_or_password"),

    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 21002, "exception.unauthorized.invalid_access_token"),

    /**
     * 22000~22999: 403 Forbidden
     */
    USER_HAS_NO_PERMISSION(HttpStatus.FORBIDDEN, 22000, "exception.forbidden.user_has_no_permission"),

    CLIENT_HAS_NO_PERMISSION(HttpStatus.FORBIDDEN, 22001, "exception.forbidden.client_has_no_permission"),

    /**
     * 23000:23999: 404 Not Found
     */
    NOT_FOUND_API(HttpStatus.NOT_FOUND, 23000, "exception.not_found.api"),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 23001, "exception.not_found.user"),

    NOT_FOUND_USER_GROUP(HttpStatus.NOT_FOUND, 23002, "exception.not_found.user_group"),

    NOT_FOUND_CLIENT(HttpStatus.NOT_FOUND, 23003, "exception.not_found.client"),

    /**
     * 24000~24999: 409 Conflict
     */
    USER_GROUP_NAME_IS_ALREADY_USED(HttpStatus.CONFLICT, 24000, "exception.conflict.user_group_name_is_already_used"),

    CLIENT_NAME_IS_ALREADY_USED(HttpStatus.CONFLICT, 24001, "exception.conflict.client_name_is_already_used"),

    EMAIL_IS_ALREADY_USED(HttpStatus.CONFLICT, 24002, "exception.conflict.email_is_already_used"),

    /**
     * 25000~25999: 500 Internal Server Error
     */
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 25000, "exception.internal_server_error.unexpected_error"),

    FAILED_TO_SEND_MAIL(HttpStatus.INTERNAL_SERVER_ERROR, 25001, "exception.internal_server_error.failed_to_send_mail");

    /**
     * HTTPステータスコード
     */
    private final HttpStatus httpStatus;

    /**
     * エラーコード
     */
    private final Integer code;

    /**
     * resources/i18n/messages.ymlのキーに対応
     */
    private final String messageKey;

}
