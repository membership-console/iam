package cc.rits.membership.console.iam.infrastructure.api.controller.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.infrastructure.api.request.EmailSendRequest;
import cc.rits.membership.console.iam.infrastructure.api.validation.RequestValidated;
import cc.rits.membership.console.iam.usecase.oauth2.email.SendEmailUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Emailコントローラ
 */
@Tag(name = "Email", description = "Eメール")
@RestController
@RequestMapping(path = "/api/oauth2/email", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class EmailRestController {

    private final SendEmailUseCase sendEmailUseCase;

    /**
     * メール送信取得API
     * 
     * @param loginClient ログインクライアント
     * @param requestBody メール送信リクエスト
     */
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail( //
        final ClientModel loginClient, //
        @RequestValidated @RequestBody final EmailSendRequest requestBody //
    ) {
        this.sendEmailUseCase.handle(loginClient, requestBody);
    }

}
