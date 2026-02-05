package brama.consultant_business_api.domain.communication.dto.request;

import brama.consultant_business_api.domain.communication.enums.CommunicationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommunicationLogCreateRequest {
    @NotBlank
    private String clientId;
    @NotBlank
    private String clientName;
    private String projectId;
    private String projectName;
    @NotNull
    private LocalDate date;
    @NotNull
    private CommunicationType type;
    @NotBlank
    private String summary;
    @NotNull
    private List<String> actionItems;
    @NotNull
    private List<String> participants;
}
