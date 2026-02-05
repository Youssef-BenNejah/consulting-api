package brama.consultant_business_api.domain.contact.model;

import brama.consultant_business_api.domain.contact.enums.ContactStatus;
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
@Document(collection = "contacts")
public class Contact {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field("company")
    private String company;

    @Field("service")
    private String service;

    @Field("budget")
    private String budget;

    @Field("message")
    private String message;

    @Field("status")
    private ContactStatus status;

    @Field("is_read")
    private boolean isRead;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;
}
