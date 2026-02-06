package brama.consultant_business_api.domain.settings.dto.request.items;

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
public class RoleUpsertRequest {
    private String id;
    private String name;
    private String description;
    private List<String> permissions;
}
