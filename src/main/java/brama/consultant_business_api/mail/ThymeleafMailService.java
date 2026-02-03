package brama.consultant_business_api.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThymeleafMailService implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from:}")
    private String fromAddress;

    @Value("${spring.mail.username:}")
    private String fallbackFrom;

    @Override
    public void sendHtml(final String to,
                         final String subject,
                         final String template,
                         final Map<String, Object> variables) {
        final Context context = new Context();
        context.setVariables(variables);
        final String html = this.templateEngine.process(template, context);

        final MimeMessage message = this.mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            final String sender = StringUtils.hasText(this.fromAddress)
                    ? this.fromAddress
                    : this.fallbackFrom;
            if (StringUtils.hasText(sender)) {
                helper.setFrom(sender);
            }
        } catch (final MessagingException ex) {
            throw new IllegalStateException("Failed to prepare email", ex);
        }

        this.mailSender.send(message);
    }
}
