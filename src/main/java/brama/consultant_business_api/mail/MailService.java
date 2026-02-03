package brama.consultant_business_api.mail;

import java.util.Map;

public interface MailService {
    void sendHtml(String to, String subject, String template, Map<String, Object> variables);
}
