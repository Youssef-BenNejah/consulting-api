package brama.consultant_business_api.domain.settings.general.model;

import brama.consultant_business_api.common.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "settings_general")
public class GeneralSettings extends BaseDocument {
    @Field("company_name")
    private String companyName;

    @Field("email")
    private String email;

    @Field("timezone")
    private String timezone;

    @Field("default_currency")
    private String defaultCurrency;

    @Field("dark_mode")
    private Boolean darkMode;

    @Field("compact_mode")
    private Boolean compactMode;
}
