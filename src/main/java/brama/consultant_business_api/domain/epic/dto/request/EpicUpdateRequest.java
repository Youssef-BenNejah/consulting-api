package brama.consultant_business_api.domain.epic.dto.request;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpicUpdateRequest {
    private String projectId;
    private String projectName;
    private String title;
    private String description;
    private Priority priority;
    private StoryStatus status;
    @Min(0)
    @Max(100)
    private Integer progress;
    @PositiveOrZero
    private Integer storyCount;
}
