package brama.consultant_business_api.domain.projecttype.dto.response;

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
public class ProjectTypeResponse {
    private String id;
    private String name;
    private String key;
    private String description;
}
