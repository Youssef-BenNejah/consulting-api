package brama.consultant_business_api.domain.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private String id;
    private String name;
    private String industry;
    private String country;
    private String primaryContact;
    private String email;
    private String phone;
    private String contractType;
    private String notes;
    private List<String> tags;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
}
