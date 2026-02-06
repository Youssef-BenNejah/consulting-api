package brama.consultant_business_api.domain.settings.general.dto.request;

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
    private String companyName;
    private String email;
    private String timezone;
    private String defaultCurrency;
    private Boolean darkMode;
    private Boolean compactMode;
}
