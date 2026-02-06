package brama.consultant_business_api.domain.settings.catalog;

import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsPriority;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsProjectStatus;
import brama.consultant_business_api.domain.settings.catalog.model.SettingsTag;

import java.util.ArrayList;
import java.util.List;

public final class SettingsCatalogDefaults {
    public static final String CATALOG_ID = "catalog";

    private SettingsCatalogDefaults() {
    }

    public static SettingsCatalog defaultCatalog() {
        return SettingsCatalog.builder()
                .id(CATALOG_ID)
                .projectStatuses(defaultProjectStatuses())
                .priorities(defaultPriorities())
                .tags(defaultTags())
                .build();
    }

    public static List<SettingsProjectStatus> defaultProjectStatuses() {
        List<SettingsProjectStatus> statuses = new ArrayList<>();
        statuses.add(SettingsProjectStatus.builder().id("draft").key("draft").name("Draft").color("bg-muted").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("discovery").key("discovery").name("Discovery").color("bg-primary").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("approved").key("approved").name("Approved").color("bg-health-green").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("delivery").key("delivery").name("Delivery").color("bg-primary").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("review").key("review").name("Review").color("bg-health-amber").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("delivered").key("delivered").name("Delivered").color("bg-health-green").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("closed").key("closed").name("Closed").color("bg-muted").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("on-hold").key("on-hold").name("On Hold").color("bg-muted").category("project").build());
        statuses.add(SettingsProjectStatus.builder().id("cancelled").key("cancelled").name("Cancelled").color("bg-health-red").category("project").build());
        return statuses;
    }

    public static List<SettingsPriority> defaultPriorities() {
        List<SettingsPriority> priorities = new ArrayList<>();
        priorities.add(SettingsPriority.builder().id("must").key("must").name("Must").color("bg-health-red").build());
        priorities.add(SettingsPriority.builder().id("should").key("should").name("Should").color("bg-health-amber").build());
        priorities.add(SettingsPriority.builder().id("could").key("could").name("Could").color("bg-primary").build());
        return priorities;
    }

    public static List<SettingsTag> defaultTags() {
        List<SettingsTag> tags = new ArrayList<>();
        tags.add(SettingsTag.builder().id("enterprise").name("enterprise").color("text-primary").build());
        tags.add(SettingsTag.builder().id("tech").name("tech").color("text-blue-400").build());
        tags.add(SettingsTag.builder().id("strategic").name("strategic").color("text-health-green").build());
        tags.add(SettingsTag.builder().id("finance").name("finance").color("text-health-amber").build());
        tags.add(SettingsTag.builder().id("health").name("health").color("text-pink-400").build());
        tags.add(SettingsTag.builder().id("compliance").name("compliance").color("text-purple-400").build());
        return tags;
    }
}
