package brama.consultant_business_api.domain.settings.general.dto.request;

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
public class GeneralSettingsPatchRequest {
    @NotBlank
    private String companyName;
    @NotBlank
    private String email;
    @NotBlank
    private String timezone;
    @NotBlank
    private String defaultCurrency;
}
