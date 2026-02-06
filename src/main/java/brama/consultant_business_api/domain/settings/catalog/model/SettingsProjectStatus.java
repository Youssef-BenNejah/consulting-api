package brama.consultant_business_api.domain.settings.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsProjectStatus {
    @Field("id")
    private String id;

    @Field("key")
    private String key;

    @Field("name")
    private String name;

    @Field("color")
    private String color;

    @Field("category")
    private String category;
}
