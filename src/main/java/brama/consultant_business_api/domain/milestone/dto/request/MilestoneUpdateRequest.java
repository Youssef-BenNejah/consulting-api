package brama.consultant_business_api.domain.milestone.dto.request;

import brama.consultant_business_api.domain.milestone.enums.MilestoneStatus;
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
public class MilestoneUpdateRequest {
    private String name;
    private String projectId;
    private String projectName;
    private LocalDate dueDate;
    private String deliverable;
    private String acceptanceCriteria;
    private MilestoneStatus status;
    private String signOffBy;
}
