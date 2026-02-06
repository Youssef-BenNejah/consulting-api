package brama.consultant_business_api.service.migration;

import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import brama.consultant_business_api.domain.settings.catalog.SettingsCatalogDefaults;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.repository.ProjectTypeRepository;
import brama.consultant_business_api.repository.SettingsCatalogRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.migrations.project-settings.enabled", havingValue = "true")
public class ProjectSettingsMigrationRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProjectSettingsMigrationRunner.class);

    private final MongoTemplate mongoTemplate;
    private final SettingsCatalogRepository settingsCatalogRepository;
    private final ProjectTypeRepository projectTypeRepository;

    @Override
    public void run(final ApplicationArguments args) {
        final SettingsCatalog catalog = settingsCatalogRepository.findById(SettingsCatalogDefaults.CATALOG_ID)
                .orElseGet(SettingsCatalogDefaults::defaultCatalog);
        final Map<String, String> statusKeyToId = new HashMap<>();
        if (catalog.getProjectStatuses() != null) {
            for (SettingsProjectStatus status : catalog.getProjectStatuses()) {
                if (status != null && status.getKey() != null) {
                    statusKeyToId.put(status.getKey().toLowerCase(), status.getId());
                }
            }
        }
        final Map<String, String> projectTypeKeyToId = new HashMap<>();
        final List<ProjectTypeConfig> projectTypes = projectTypeRepository.findAll();
        for (ProjectTypeConfig type : projectTypes) {
            if (type != null && type.getKey() != null) {
                projectTypeKeyToId.put(type.getKey().toLowerCase(), type.getId());
            }
        }

        final MongoCollection<Document> collection = mongoTemplate.getCollection("projects");
        final org.bson.conversions.Bson filter = Filters.or(
                Filters.exists("status_id", false),
                Filters.exists("project_type_id", false)
        );

        int updated = 0;
        int skipped = 0;
        for (Document doc : collection.find(filter)) {
            final String id = doc.getObjectId("_id").toHexString();
            final String statusKey = doc.getString("status");
            final String typeKey = doc.getString("type");
            final String statusId = statusKey == null ? null : statusKeyToId.get(statusKey.toLowerCase());
            final String typeId = typeKey == null ? null : projectTypeKeyToId.get(typeKey.toLowerCase());

            final java.util.List<org.bson.conversions.Bson> updates = new java.util.ArrayList<>();
            if (statusId != null && doc.get("status_id") == null) {
                updates.add(Updates.set("status_id", statusId));
            }
            if (typeId != null && doc.get("project_type_id") == null) {
                updates.add(Updates.set("project_type_id", typeId));
            }
            if (doc.containsKey("status")) {
                updates.add(Updates.unset("status"));
            }
            if (doc.containsKey("type")) {
                updates.add(Updates.unset("type"));
            }

            if (!updates.isEmpty()) {
                collection.updateOne(Filters.eq("_id", doc.getObjectId("_id")), Updates.combine(updates));
                updated++;
            } else {
                skipped++;
                logger.warn("Project migration skipped (missing mapping). projectId={}", id);
            }
        }

        logger.info("Project settings migration complete. updated={}, skipped={}", updated, skipped);
    }
}
