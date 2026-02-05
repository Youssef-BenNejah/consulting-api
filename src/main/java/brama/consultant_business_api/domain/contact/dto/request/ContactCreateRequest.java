package brama.consultant_business_api.domain.contact.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreateRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    private String company;
    private String service;
    private String budget;
    @NotBlank
    private String message;
}
