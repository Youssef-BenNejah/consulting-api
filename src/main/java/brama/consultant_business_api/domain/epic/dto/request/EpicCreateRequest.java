package brama.consultant_business_api.domain.epic.dto.request;

import brama.consultant_business_api.domain.common.enums.Priority;
import brama.consultant_business_api.domain.common.enums.StoryStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EpicCreateRequest {
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Priority priority;
    @NotNull
    private StoryStatus status;
    @NotNull
    @Min(0)
    @Max(100)
    private Integer progress;
    @NotNull
    @PositiveOrZero
    private Integer storyCount;
}
