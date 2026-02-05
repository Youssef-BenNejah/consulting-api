package brama.consultant_business_api.domain.client.dto.request;

import jakarta.validation.constraints.Email;
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
public class ClientUpdateRequest {
    private String name;
    private String industry;
    private String country;
    private String primaryContact;
    @Email
    private String email;
    private String phone;
    private String contractType;
    private String notes;
    private List<String> tags;
}
