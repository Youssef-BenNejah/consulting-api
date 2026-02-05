package brama.consultant_business_api.domain.userstory.dto.request;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserStoryCreateRequest {
    @NotBlank
    private String epicId;
    @NotBlank
    private String epicTitle;
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private List<String> acceptanceCriteria;
    @NotNull
    private Priority priority;
    @NotNull
    private StoryStatus status;
    @NotNull
    @PositiveOrZero
    private Integer effort;
    @NotBlank
    private String notes;
}
