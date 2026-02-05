package brama.consultant_business_api.domain.userstory.model;

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
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_stories")
public class UserStory extends BaseDocument {
    @Field("epic_id")
    private String epicId;

    @Field("epic_title")
    private String epicTitle;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("acceptance_criteria")
    private List<String> acceptanceCriteria;

    @Field("priority")
    private Priority priority;

    @Field("status")
    private StoryStatus status;

    @Field("effort")
    private Integer effort;

    @Field("notes")
    private String notes;

    @Field("created_at")
    private LocalDate createdAt;
}
