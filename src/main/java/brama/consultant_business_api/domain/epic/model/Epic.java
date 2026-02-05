package brama.consultant_business_api.domain.epic.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "epics")
public class Epic extends BaseDocument {
    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("priority")
    private Priority priority;

    @Field("status")
    private StoryStatus status;

    @Field("progress")
    private Integer progress;

    @Field("story_count")
    private Integer storyCount;

    @Field("created_at")
    private LocalDate createdAt;
}
