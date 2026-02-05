package brama.consultant_business_api.domain.milestone.dto.request;

import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MilestoneCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String projectId;
    @NotBlank
    private String projectName;
    @NotNull
    private LocalDate dueDate;
    @NotBlank
    private String deliverable;
    @NotBlank
    private String acceptanceCriteria;
    @NotNull
    private MilestoneStatus status;
    @NotBlank
    private String signOffBy;
}
