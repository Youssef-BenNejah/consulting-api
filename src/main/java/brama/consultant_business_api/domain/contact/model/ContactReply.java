package brama.consultant_business_api.domain.contact.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_replies")
public class ContactReply {
    @Id
    private String id;

    @Field("contact_id")
    private String contactId;

    @Field("message")
    private String message;

    @Field("sent_at")
    private Instant sentAt;
}
