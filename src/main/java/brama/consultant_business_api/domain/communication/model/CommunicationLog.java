package brama.consultant_business_api.domain.communication.model;

import brama.consultant_business_api.common.BaseDocument;
import brama.consultant_business_api.domain.communication.enums.CommunicationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "communication_logs")
public class CommunicationLog extends BaseDocument {
    @Field("client_id")
    private String clientId;

    @Field("client_name")
    private String clientName;

    @Field("project_id")
    private String projectId;

    @Field("project_name")
    private String projectName;

    @Field("date")
    private LocalDate date;

    @Field("type")
    private CommunicationType type;

    @Field("summary")
    private String summary;

    @Field("action_items")
    private List<String> actionItems;

    @Field("participants")
    private List<String> participants;
}
