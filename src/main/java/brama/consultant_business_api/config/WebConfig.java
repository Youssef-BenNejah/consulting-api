package brama.consultant_business_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final StringToEnumConverterFactory converterFactory;

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverterFactory(converterFactory);
    }
}
