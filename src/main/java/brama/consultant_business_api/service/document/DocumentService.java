package brama.consultant_business_api.service.document;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.document.dto.response.DocumentDownload;
import brama.consultant_business_api.domain.document.dto.response.DocumentResponse;
import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface DocumentService {
    PagedResult<DocumentResponse> search(String search,
                                         DocumentCategoryKey category,
                                         String clientId,
                                         String projectId,
                                         LocalDate dateFrom,
                                         LocalDate dateTo,
                                         Integer page,
                                         Integer size);

    DocumentResponse createMetadata(DocumentCreateRequest request);

    DocumentResponse upload(MultipartFile file, DocumentCreateRequest request);

    DocumentResponse getById(String id);

    void delete(String id);

    DocumentDownload download(String id);
}
