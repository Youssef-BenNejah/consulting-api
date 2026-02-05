package brama.consultant_business_api.domain.communication.dto.response;

import brama.consultant_business_api.domain.communication.enums.CommunicationType;
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
public class CommunicationLogResponse {
    private String id;
    private String clientId;
    private String clientName;
    private String projectId;
    private String projectName;
    private LocalDate date;
    private CommunicationType type;
    private String summary;
    private List<String> actionItems;
    private List<String> participants;
}
