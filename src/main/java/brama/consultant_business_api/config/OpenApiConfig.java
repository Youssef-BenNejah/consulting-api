package brama.consultant_business_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import brama.consultant_business_api.common.ApiErrorResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.core.converter.ModelConverters;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Best Backend",
                        email = "contact@attia.com",
                        url = "https://attia.com"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://attia.com/licence"),
                termsOfService = "Terms of service"),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://your-prod-url.com"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    @Bean
    public GlobalOpenApiCustomizer globalErrorResponsesCustomiser() {
        return openApi -> {
            ensureErrorSchema(openApi);
            addStandardErrorResponses(openApi);
            normalizeParameterConstraints(openApi);
        };
    }

    @Bean
    public GroupedOpenApi defaultOpenApi(final GlobalOpenApiCustomizer customizer) {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(customizer)
                .build();
    }

    private void ensureErrorSchema(final OpenAPI openApi) {
        Components components = openApi.getComponents();
        if (components == null) {
            components = new Components();
            openApi.setComponents(components);
        }
        final String schemaName = "ApiErrorResponse";
        if (components.getSchemas() == null || !components.getSchemas().containsKey(schemaName)) {
            final var schemas = ModelConverters.getInstance().read(ApiErrorResponse.class);
            schemas.forEach(components::addSchemas);
        }
        normalizeComponentSchemas(components);
    }

    private void addStandardErrorResponses(final OpenAPI openApi) {
        if (openApi.getPaths() == null) {
            return;
        }
        openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(this::ensureErrorResponses));
    }

    private void ensureErrorResponses(final Operation operation) {
        if (operation == null) {
            return;
        }
        final ApiResponses responses = operation.getResponses();
        addErrorResponseIfMissing(responses, "400", "Bad Request");
        addErrorResponseIfMissing(responses, "401", "Unauthorized");
        addErrorResponseIfMissing(responses, "403", "Forbidden");
        addErrorResponseIfMissing(responses, "404", "Not Found");
        addErrorResponseIfMissing(responses, "409", "Conflict");
        addErrorResponseIfMissing(responses, "500", "Internal Server Error");
    }

    private void addErrorResponseIfMissing(final ApiResponses responses, final String code, final String description) {
        if (responses == null || responses.containsKey(code)) {
            return;
        }
        final ApiResponse response = new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse"))));
        responses.addApiResponse(code, response);
    }

    private void normalizeParameterConstraints(final OpenAPI openApi) {
        if (openApi.getPaths() == null) {
            return;
        }
        openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            if (operation.getParameters() == null) {
                return;
            }
            for (Parameter parameter : operation.getParameters()) {
                if (parameter == null || parameter.getSchema() == null) {
                    continue;
                }
                applyIdPattern(parameter);
                applyPageSizeLimits(parameter);
                applyRequiredStringMinLength(parameter);
            }
        }));
    }

    private void normalizeComponentSchemas(final Components components) {
        if (components == null || components.getSchemas() == null) {
            return;
        }
        components.getSchemas().values().forEach(schema -> {
            if (schema == null || schema.getProperties() == null) {
                return;
            }
            if (schema.getRequired() != null) {
                for (Object requiredNameObj : schema.getRequired()) {
                    final String requiredName = String.valueOf(requiredNameObj);
                    final Schema<?> prop = (Schema<?>) schema.getProperties().get(requiredName);
                    if (prop != null && "string".equals(prop.getType()) && prop.getMinLength() == null) {
                        prop.setMinLength(1);
                    }
                }
            }
            schema.getProperties().forEach((name, prop) -> {
                final String propName = name != null ? String.valueOf(name) : null;
                if (prop instanceof Schema<?> schemaProp) {
                    final String type = schemaProp.getType();
                    if (("integer".equals(type) || "number".equals(type))
                            && schemaProp.getMinimum() == null
                            && shouldApplyMinimum(propName)) {
                        schemaProp.setMinimum(java.math.BigDecimal.ZERO);
                    }
                }
            });
        });
    }

    private boolean shouldApplyMinimum(final String name) {
        if (name == null) {
            return true;
        }
        final String lower = name.toLowerCase();
        return !lower.contains("margin");
    }

    private void applyIdPattern(final Parameter parameter) {
        final String name = parameter.getName();
        if (name == null) {
            return;
        }
        final boolean isIdParam = "id".equalsIgnoreCase(name) || name.toLowerCase().endsWith("id");
        if (!isIdParam) {
            return;
        }
        final Schema<?> schema = parameter.getSchema();
        if (schema == null) {
            return;
        }
        if (schema.getPattern() == null) {
            schema.setPattern("^[a-fA-F0-9]{24}$");
        }
    }

    private void applyPageSizeLimits(final Parameter parameter) {
        final String name = parameter.getName();
        if (!"page".equalsIgnoreCase(name) && !"size".equalsIgnoreCase(name)) {
            return;
        }
        final Schema<?> schema = parameter.getSchema();
        if (schema == null) {
            return;
        }
        if ("page".equalsIgnoreCase(name)) {
            if (schema.getMinimum() == null) {
                schema.setMinimum(java.math.BigDecimal.ONE);
            }
        }
        if ("size".equalsIgnoreCase(name)) {
            if (schema.getMinimum() == null) {
                schema.setMinimum(java.math.BigDecimal.ONE);
            }
            if (schema.getMaximum() == null) {
                schema.setMaximum(java.math.BigDecimal.valueOf(200));
            }
        }
    }

    private void applyRequiredStringMinLength(final Parameter parameter) {
        if (!Boolean.TRUE.equals(parameter.getRequired())) {
            return;
        }
        final Schema<?> schema = parameter.getSchema();
        if (schema == null) {
            return;
        }
        if ((schema.getMinLength() == null) && "string".equals(schema.getType())) {
            schema.setMinLength(1);
        }
    }
}

