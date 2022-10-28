package cc.rits.membership.console.iam.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.request.ClientUpsertRequest;
import cc.rits.membership.console.iam.infrastructure.api.response.ClientCredentialsResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.ClientResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.ClientsResponse;
import cc.rits.membership.console.iam.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.iam.usecase.CreateClientUseCase;
import cc.rits.membership.console.iam.usecase.GetClientsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * クライアントコントローラ
 */
@Tag(name = "Client", description = "クライアント")
@RestController
@RequestMapping(path = "/api/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class ClientRestController {

    private final GetClientsUseCase getClientsUseCase;

    private final CreateClientUseCase createClientUseCase;

    /**
     * クライアントリスト取得API
     *
     * @param loginUser ログインユーザ
     * @return クライアントリスト
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClientsResponse getClients( //
        final UserModel loginUser //
    ) {
        final var clients = this.getClientsUseCase.handle(loginUser).stream() //
            .map(ClientResponse::new) //
            .collect(Collectors.toList());
        return new ClientsResponse(clients);
    }

    /**
     * クライアント作成API
     *
     * @param loginUser ログインユーザ
     * @param requestBody クライアント作成リクエスト
     * @return クレデンシャル
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientCredentialsResponse createClient( //
        final UserModel loginUser, //
        @RequestValidated @RequestBody final ClientUpsertRequest requestBody //
    ) {
        return new ClientCredentialsResponse(this.createClientUseCase.handle(loginUser, requestBody));
    }

    /**
     * クライアント更新API
     *
     * @param loginUser ログインユーザ
     * @param id ID
     * @param requestBody クライアント更新リクエスト
     */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateClient( //
        final UserModel loginUser, //
        @PathVariable("id") final String id, //
        @RequestValidated @RequestBody final ClientUpsertRequest requestBody //
    ) {
        // TODO: クライアント更新APIを実装
    }

    /**
     * クライアント削除API
     *
     * @param loginUser ログインユーザ
     * @param id ID
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient( //
        final UserModel loginUser, //
        @PathVariable("id") final String id //
    ) {
        // TODO: クライアント削除APIを実装
    }

}
