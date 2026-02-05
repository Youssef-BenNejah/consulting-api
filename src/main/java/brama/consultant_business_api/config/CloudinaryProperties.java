package brama.consultant_business_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryProperties {
    private boolean enabled = true;
    private String cloudName;
    private String apiKey;
    private String apiSecret;
    private String folder = "consulting-cherif";
}
