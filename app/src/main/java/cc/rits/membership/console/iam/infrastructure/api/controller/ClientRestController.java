package cc.rits.membership.console.iam.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.infrastructure.api.response.ClientResponse;
import cc.rits.membership.console.iam.infrastructure.api.response.ClientsResponse;
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

}
