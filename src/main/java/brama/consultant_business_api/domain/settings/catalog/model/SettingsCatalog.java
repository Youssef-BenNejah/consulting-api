package brama.consultant_business_api.domain.settings.catalog.model;

import brama.consultant_business_api.common.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "settings_catalog")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsCatalog extends BaseDocument {
    @Field("project_statuses")
    private List<SettingsProjectStatus> projectStatuses;

    @Field("priorities")
    private List<SettingsPriority> priorities;

    @Field("tags")
    private List<SettingsTag> tags;
}
