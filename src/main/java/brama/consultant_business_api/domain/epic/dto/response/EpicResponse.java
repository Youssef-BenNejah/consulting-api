package brama.consultant_business_api.domain.epic.dto.response;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpicResponse {
    private String id;
    private String projectId;
    private String projectName;
    private String title;
    private String description;
    private Priority priority;
    private StoryStatus status;
    private Integer progress;
    private Integer storyCount;
    private LocalDate createdAt;
}
