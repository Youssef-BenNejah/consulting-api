package brama.consultant_business_api.domain.document.dto.response;

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
public class DocumentDownload {
    private byte[] content;
    private String filename;
    private String contentType;
}
