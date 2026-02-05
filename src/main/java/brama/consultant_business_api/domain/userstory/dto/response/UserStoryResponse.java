package brama.consultant_business_api.domain.userstory.dto.response;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryResponse {
    private String id;
    private String epicId;
    private String epicTitle;
    private String projectId;
    private String projectName;
    private String title;
    private String description;
    private List<String> acceptanceCriteria;
    private Priority priority;
    private StoryStatus status;
    private Integer effort;
    private String notes;
    private LocalDate createdAt;
}
