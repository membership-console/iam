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
     * 400 Bad Request
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "exception.bad_request.invalid_request_parameter"),

    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "exception.bad_request.invalid_password_length"),

    PASSWORD_IS_TOO_SIMPLE(HttpStatus.BAD_REQUEST, "exception.bad_request.password_is_too_simple"),

    /**
     * 401 Unauthorized
     */
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "exception.unauthorized.user_not_logged_in"),

    INCORRECT_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "exception.unauthorized.incorrect_email_or_password"),

    /**
     * 403 Forbidden
     */
    USER_HAS_NO_PERMISSION(HttpStatus.FORBIDDEN, "exception.forbidden.user_has_no_permission"),

    /**
     * 404 Not Found
     */
    NOT_FOUND_API(HttpStatus.NOT_FOUND, "exception.not_found.api"),

    NOT_FOUND_USER_GROUP(HttpStatus.NOT_FOUND, "exception.not_found.user_group"),

    /**
     * 500 Internal Server Error
     */
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "exception.internal_server_error.unexpected_error");

    /**
     * HTTPステータスコード
     */
    private final HttpStatus status;

    /**
     * resources/i18n/messages.ymlのキーに対応
     */
    private final String messageKey;

}
