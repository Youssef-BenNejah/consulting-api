package brama.consultant_business_api.domain.client.model;

import brama.consultant_business_api.common.BaseDocument;
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
@Document(collection = "clients")
public class Client extends BaseDocument {
    @Field("name")
    private String name;

    @Field("industry")
    private String industry;

    @Field("country")
    private String country;

    @Field("primary_contact")
    private String primaryContact;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("contract_type")
    private String contractType;

    @Field("notes")
    private String notes;

    @Field("tags")
    private List<String> tags;

    @Field("created_at")
    private LocalDate createdAt;
}
