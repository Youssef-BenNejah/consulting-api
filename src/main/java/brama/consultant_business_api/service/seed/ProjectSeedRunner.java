package brama.consultant_business_api.service.seed;

import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import brama.consultant_business_api.domain.contracttype.model.ContractTypeConfig;
import brama.consultant_business_api.domain.settings.catalog.SettingsCatalogDefaults;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsPriority;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsTag;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.ProjectTypeRepository;
import brama.consultant_business_api.repository.ContractTypeRepository;
import brama.consultant_business_api.repository.SettingsCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.projects.enabled", havingValue = "true")
public class ProjectSeedRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProjectSeedRunner.class);

    private final ProjectRepository projectRepository;
    private final SettingsCatalogRepository settingsCatalogRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ContractTypeRepository contractTypeRepository;

    @Override
    public void run(final ApplicationArguments args) {
        final SettingsCatalog catalog = settingsCatalogRepository.findById(SettingsCatalogDefaults.CATALOG_ID)
                .orElseGet(SettingsCatalogDefaults::defaultCatalog);
        final Map<String, String> statusKeyToId = mapStatusIds(catalog);
        final Map<String, String> priorityKeyToId = mapPriorityIds(catalog);
        final Map<String, String> tagNameToId = mapTagIds(catalog);
        final Map<String, String> projectTypeKeyToId = mapProjectTypeIds();
        final Map<String, String> contractTypeKeyToId = mapContractTypeIds();

        require(statusKeyToId, "draft", "project status draft");
        require(projectTypeKeyToId, "fixed", "project type fixed");
        final String defaultStatusId = statusKeyToId.get("draft");
        final String defaultProjectTypeId = projectTypeKeyToId.get("fixed");

        projectRepository.deleteAll();
        final List<Project> projects = List.of(
                project("PRJ-1001", "CL-100", "Atlas Group", "Website Revamp", "Rebuild corporate site with CMS and SEO",
                        LocalDate.of(2026, 1, 6), LocalDate.of(2026, 4, 18),
                        statusId(statusKeyToId, defaultStatusId, "discovery"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "fixed"),
                        priorityKeyToId.get("must"), tags(tagNameToId, "tech", "enterprise"),
                        contractTypeId(contractTypeKeyToId, "enterprise"), 85000D, 22000D, 18000D, HealthStatus.AMBER, 35),
                project("PRJ-1002", "CL-101", "Nova Health", "Patient Portal", "Secure portal for appointments and records",
                        LocalDate.of(2025, 11, 12), LocalDate.of(2026, 3, 28),
                        statusId(statusKeyToId, defaultStatusId, "delivery"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "fixed"),
                        priorityKeyToId.get("must"), tags(tagNameToId, "health", "compliance"),
                        contractTypeId(contractTypeKeyToId, "enterprise"), 120000D, 42000D, 26000D, HealthStatus.AMBER, 58),
                project("PRJ-1003", "CL-102", "BluePeak Finance", "Risk Dashboard", "Real-time risk monitoring dashboard",
                        LocalDate.of(2025, 9, 1), LocalDate.of(2026, 2, 14),
                        statusId(statusKeyToId, defaultStatusId, "review"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "tm"),
                        priorityKeyToId.get("should"), tags(tagNameToId, "finance", "strategic"),
                        contractTypeId(contractTypeKeyToId, "consulting"), 90000D, 30000D, 28000D, HealthStatus.AMBER, 72),
                project("PRJ-1004", "CL-103", "Urban Mobility", "Fleet Optimization", "Predictive maintenance and routing",
                        LocalDate.of(2025, 7, 15), LocalDate.of(2026, 1, 30),
                        statusId(statusKeyToId, defaultStatusId, "delivered"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "fixed"),
                        priorityKeyToId.get("could"), tags(tagNameToId, "tech"),
                        contractTypeId(contractTypeKeyToId, "project"), 65000D, 18000D, 16000D, HealthStatus.GREEN, 100),
                project("PRJ-1005", "CL-104", "GreenGrid Energy", "Analytics Platform", "Usage analytics for smart meters",
                        LocalDate.of(2026, 2, 1), LocalDate.of(2026, 6, 20),
                        statusId(statusKeyToId, defaultStatusId, "approved"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "retainer"),
                        priorityKeyToId.get("should"), tags(tagNameToId, "strategic"),
                        contractTypeId(contractTypeKeyToId, "retainer"), 140000D, 50000D, 34000D, HealthStatus.GREEN, 12),
                project("PRJ-1006", "CL-105", "CivicWorks", "Permit Workflow", "Digitize permit approvals and tracking",
                        LocalDate.of(2025, 10, 5), LocalDate.of(2026, 5, 10),
                        statusId(statusKeyToId, defaultStatusId, "delivery"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "fixed"),
                        priorityKeyToId.get("must"), tags(tagNameToId, "compliance"),
                        contractTypeId(contractTypeKeyToId, "project"), 110000D, 36000D, 29000D, HealthStatus.AMBER, 45),
                project("PRJ-1007", "CL-106", "MarketPulse", "Insights Engine", "Customer segmentation and insights",
                        LocalDate.of(2025, 8, 20), LocalDate.of(2026, 2, 28),
                        statusId(statusKeyToId, defaultStatusId, "review"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "tm"),
                        priorityKeyToId.get("should"), tags(tagNameToId, "tech"),
                        contractTypeId(contractTypeKeyToId, "consulting"), 78000D, 24000D, 21000D, HealthStatus.AMBER, 66),
                project("PRJ-1008", "CL-107", "Skyline Retail", "Omnichannel Refresh", "Unified storefront and inventory",
                        LocalDate.of(2025, 6, 1), LocalDate.of(2025, 12, 15),
                        statusId(statusKeyToId, defaultStatusId, "closed"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "fixed"),
                        priorityKeyToId.get("could"), tags(tagNameToId, "enterprise"),
                        contractTypeId(contractTypeKeyToId, "project"), 95000D, 28000D, 24000D, HealthStatus.GREEN, 100),
                project("PRJ-1009", "CL-108", "AeroLogix", "Operations Console", "Internal tooling for ops monitoring",
                        LocalDate.of(2026, 1, 20), LocalDate.of(2026, 5, 30),
                        statusId(statusKeyToId, defaultStatusId, "delivery"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "fixed"),
                        priorityKeyToId.get("must"), tags(tagNameToId, "tech", "strategic"),
                        contractTypeId(contractTypeKeyToId, "enterprise"), 130000D, 46000D, 32000D, HealthStatus.AMBER, 52),
                project("PRJ-1010", "CL-109", "BrightHR", "Onboarding Automation", "Automate onboarding workflows",
                        LocalDate.of(2025, 11, 1), LocalDate.of(2026, 1, 25),
                        statusId(statusKeyToId, defaultStatusId, "cancelled"),
                        projectTypeId(projectTypeKeyToId, defaultProjectTypeId, "retainer"),
                        priorityKeyToId.get("could"), tags(tagNameToId, "enterprise"),
                        contractTypeId(contractTypeKeyToId, "retainer"), 40000D, 10000D, 9000D, HealthStatus.RED, 10)
        );

        projectRepository.saveAll(projects);
        logger.info("Project seed complete. inserted={}", projects.size());
    }

    private Project project(final String projectId,
                            final String clientId,
                            final String clientName,
                            final String name,
                            final String description,
                            final LocalDate startDate,
                            final LocalDate endDate,
                            final String statusId,
                            final String projectTypeId,
                            final String priorityId,
                            final List<String> tagIds,
                            final String contractTypeId,
                            final Double clientBudget,
                            final Double vendorCost,
                            final Double internalCost,
                            final HealthStatus healthStatus,
                            final Integer progress) {
        return Project.builder()
                .projectId(projectId)
                .clientId(clientId)
                .clientName(clientName)
                .name(name)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .statusId(statusId)
                .projectTypeId(projectTypeId)
                .priorityId(priorityId)
                .tagIds(tagIds)
                .contractTypeId(contractTypeId)
                .clientBudget(clientBudget)
                .vendorCost(vendorCost)
                .internalCost(internalCost)
                .healthStatus(healthStatus)
                .progress(progress)
                .build();
    }

    private Map<String, String> mapStatusIds(final SettingsCatalog catalog) {
        final Map<String, String> map = new HashMap<>();
        if (catalog.getProjectStatuses() == null) {
            return map;
        }
        for (SettingsProjectStatus status : catalog.getProjectStatuses()) {
            if (status != null && status.getKey() != null) {
                map.put(status.getKey().toLowerCase(), status.getId());
            }
        }
        return map;
    }

    private Map<String, String> mapPriorityIds(final SettingsCatalog catalog) {
        final Map<String, String> map = new HashMap<>();
        if (catalog.getPriorities() == null) {
            return map;
        }
        for (SettingsPriority priority : catalog.getPriorities()) {
            if (priority != null && priority.getKey() != null) {
                map.put(priority.getKey().toLowerCase(), priority.getId());
            }
        }
        return map;
    }

    private Map<String, String> mapTagIds(final SettingsCatalog catalog) {
        final Map<String, String> map = new HashMap<>();
        if (catalog.getTags() == null) {
            return map;
        }
        for (SettingsTag tag : catalog.getTags()) {
            if (tag != null && tag.getName() != null) {
                map.put(tag.getName().toLowerCase(), tag.getId());
            }
        }
        return map;
    }

    private Map<String, String> mapProjectTypeIds() {
        final Map<String, String> map = new HashMap<>();
        for (ProjectTypeConfig type : projectTypeRepository.findAll()) {
            if (type != null && type.getKey() != null) {
                map.put(type.getKey().toLowerCase(), type.getId());
            }
        }
        return map;
    }

    private Map<String, String> mapContractTypeIds() {
        final Map<String, String> map = new HashMap<>();
        for (ContractTypeConfig type : contractTypeRepository.findAll()) {
            if (type != null && type.getKey() != null) {
                map.put(type.getKey().toLowerCase(), type.getId());
            }
        }
        return map;
    }

    private void require(final Map<String, String> map, final String key, final String label) {
        if (!map.containsKey(key)) {
            throw new IllegalStateException("Missing " + label + " in settings. key=" + key);
        }
    }

    private String statusId(final Map<String, String> statusKeyToId, final String defaultId, final String key) {
        final String id = statusKeyToId.get(key);
        return id != null ? id : defaultId;
    }

    private String projectTypeId(final Map<String, String> projectTypeKeyToId, final String defaultId, final String key) {
        final String id = projectTypeKeyToId.get(key);
        return id != null ? id : defaultId;
    }

    private String contractTypeId(final Map<String, String> contractTypeKeyToId, final String key) {
        if (key == null) {
            return null;
        }
        return contractTypeKeyToId.get(key);
    }

    private List<String> tags(final Map<String, String> tagNameToId, final String... names) {
        final java.util.ArrayList<String> ids = new java.util.ArrayList<>();
        for (String name : names) {
            if (name == null) {
                continue;
            }
            final String id = tagNameToId.get(name.toLowerCase());
            if (id != null) {
                ids.add(id);
            }
        }
        return ids.isEmpty() ? null : ids;
    }
}
