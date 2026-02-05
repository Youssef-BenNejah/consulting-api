package brama.consultant_business_api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {
    @Override
    public <T extends Enum> Converter<String, T> getConverter(final Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private static final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        private StringToEnumConverter(final Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(final String source) {
            if (source == null) {
                return null;
            }
            final String normalized = source.trim()
                    .toUpperCase()
                    .replace("-", "_")
                    .replace(" ", "_");
            return (T) Enum.valueOf(enumType, normalized);
        }
    }
}
