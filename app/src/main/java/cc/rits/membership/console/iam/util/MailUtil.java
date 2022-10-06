package cc.rits.membership.console.iam.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.InternalServerErrorException;
import cc.rits.membership.console.iam.property.SendGridProperty;
import lombok.RequiredArgsConstructor;

/**
 * メールユーティリティ
 */
@Component
@RequiredArgsConstructor
public class MailUtil {

    private final SendGridProperty sendGridProperty;

    /**
     * メール送信
     * 
     * @param tos 宛先リスト
     * @param subject 件名
     * @param body 本文
     */
    public void send(final List<String> tos, final String subject, final String body) {
        if (!this.sendGridProperty.getEnabled()) {
            return;
        }

        // メールオブジェクトを作成
        final var mail = new Mail();
        mail.setFrom(new Email(this.sendGridProperty.getFrom().getEmail(), this.sendGridProperty.getFrom().getName()));
        mail.setSubject(subject);
        mail.addContent(new Content("text/plain", body));
        final var personalization = new Personalization();
        tos.forEach(to -> personalization.addTo(new Email(to)));
        mail.addPersonalization(personalization);

        // メール送信
        try {
            final var sendGrid = new SendGrid(this.sendGridProperty.getApiKey());
            final var request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            final var response = sendGrid.api(request);
            if (!HttpStatus.valueOf(response.getStatusCode()).is2xxSuccessful()) {
                throw new InternalServerErrorException(ErrorCode.FAILED_TO_SEND_MAIL);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(ErrorCode.FAILED_TO_SEND_MAIL);
        }
    }

}
