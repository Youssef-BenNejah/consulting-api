package brama.consultant_business_api.domain.userstory.dto.request;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryUpdateRequest {
    private String epicId;
    private String epicTitle;
    private String projectId;
    private String projectName;
    private String title;
    private String description;
    private List<String> acceptanceCriteria;
    private Priority priority;
    private StoryStatus status;
    @PositiveOrZero
    private Integer effort;
    private String notes;
}
