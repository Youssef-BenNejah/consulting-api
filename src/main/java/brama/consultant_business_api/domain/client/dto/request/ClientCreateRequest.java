package brama.consultant_business_api.domain.client.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ClientCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String industry;
    @NotBlank
    private String country;
    @NotBlank
    private String primaryContact;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String contractType;
    @NotBlank
    private String notes;
    @NotNull
    private List<String> tags;
}
