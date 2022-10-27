package cc.rits.membership.console.iam.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.response.OAuthClientsResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * OAuthクライアントコントローラ
 */
@Tag(name = "OAuth Client", description = "OAuth Client")
@RestController
@RequestMapping(path = "/api/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class OAuthClientRestController {

    /**
     * OAuthクライアントリスト取得API
     *
     * @param loginUser ログインユーザ
     * @return OAuthクライアントリスト
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public OAuthClientsResponse getUserGroups( //
        final UserModel loginUser //
    ) {
        // TODO: 実装する
        return null;
    }

}
