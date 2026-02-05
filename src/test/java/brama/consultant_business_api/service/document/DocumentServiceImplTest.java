package brama.consultant_business_api.service.document;

import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.document.mapper.DocumentMapper;
import brama.consultant_business_api.domain.document.model.DocumentFile;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.DocumentRepository;
import brama.consultant_business_api.service.document.impl.DocumentServiceImpl;
import brama.consultant_business_api.service.storage.CloudinaryService;
import brama.consultant_business_api.config.CloudinaryProperties;
import brama.consultant_business_api.support.TestUploadFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {
    @Mock
    private DocumentRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private GridFsTemplate gridFsTemplate;
    @Mock
    private CloudinaryService cloudinaryService;

    private DocumentServiceImpl service;
    private CloudinaryProperties cloudinaryProperties;

    @BeforeEach
    void setUp() {
        cloudinaryProperties = new CloudinaryProperties();
        cloudinaryProperties.setEnabled(false);
        service = new DocumentServiceImpl(repository, mongoTemplate, new DocumentMapper(), gridFsTemplate, cloudinaryService, cloudinaryProperties);
    }

    @Test
    void createMetadataSavesAndReturns() {
        DocumentCreateRequest request = DocumentCreateRequest.builder()
                .name("file.pdf")
                .category(DocumentCategoryKey.CONTRACT)
                .projectId("p1")
                .uploadedBy("Admin")
                .uploadedAt(LocalDate.now())
                .size("1 KB")
                .fileType("PDF")
                .build();

        when(repository.save(any(DocumentFile.class))).thenAnswer(invocation -> {
            DocumentFile saved = invocation.getArgument(0);
            saved.setId("d1");
            return saved;
        });

        var response = service.createMetadata(request);
        assertThat(response.getId()).isEqualTo("d1");
    }

    @Test
    void uploadMissingFileThrows() {
        DocumentCreateRequest request = DocumentCreateRequest.builder()
                .category(DocumentCategoryKey.CONTRACT)
                .projectId("p1")
                .uploadedBy("Admin")
                .build();

        assertThatThrownBy(() -> service.upload(null, request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void uploadStoresFileAndSaves() {
        TestUploadFile.assumeExists();
        DocumentCreateRequest request = DocumentCreateRequest.builder()
                .category(DocumentCategoryKey.CONTRACT)
                .projectId("p1")
                .uploadedBy("Admin")
                .build();

        when(cloudinaryService.isEnabled()).thenReturn(false);

        MockMultipartFile file = TestUploadFile.mockMultipartFile();

        when(gridFsTemplate.store(any(), eq("1600w-a1RYzvS1EFo.webp"), eq("image/webp"))).thenReturn(new ObjectId());
        when(repository.save(any(DocumentFile.class))).thenAnswer(invocation -> {
            DocumentFile saved = invocation.getArgument(0);
            saved.setId("d1");
            return saved;
        });

        var response = service.upload(file, request);
        assertThat(response.getId()).isEqualTo("d1");
        assertThat(response.getFileId()).isNotNull();
    }

    @Test
    void uploadUsesCloudinaryWhenEnabled() {
        TestUploadFile.assumeExists();
        DocumentCreateRequest request = DocumentCreateRequest.builder()
                .category(DocumentCategoryKey.CONTRACT)
                .projectId("p1")
                .uploadedBy("Admin")
                .build();

        when(cloudinaryService.isEnabled()).thenReturn(true);
        when(cloudinaryService.upload(any(), any())).thenReturn(
                new CloudinaryService.CloudinaryUploadResult("public-id", "https://cloudinary.example/file", "image")
        );

        MockMultipartFile file = TestUploadFile.mockMultipartFile();

        when(repository.save(any(DocumentFile.class))).thenAnswer(invocation -> {
            DocumentFile saved = invocation.getArgument(0);
            saved.setId("d1");
            return saved;
        });

        var response = service.upload(file, request);
        assertThat(response.getId()).isEqualTo("d1");
        assertThat(response.getStorageProvider()).isEqualTo("cloudinary");
        assertThat(response.getFileUrl()).isEqualTo("https://cloudinary.example/file");
        assertThat(response.getFileId()).isEqualTo("public-id");
        assertThat(response.getResourceType()).isEqualTo("image");

        verify(cloudinaryService).upload(any(), any());
        verify(gridFsTemplate, never()).store(any(), any(), Optional.ofNullable(any()));
    }

    @Test
    void deleteRemovesGridFsFile() {
        DocumentFile document = DocumentFile.builder()
                .id("d1")
                .fileId(new ObjectId().toHexString())
                .build();

        when(repository.findById("d1")).thenReturn(Optional.of(document));

        service.delete("d1");

        verify(repository).deleteById("d1");
        verify(gridFsTemplate).delete(any(Query.class));
    }

    @Test
    void downloadMissingFileThrows() {
        DocumentFile document = DocumentFile.builder()
                .id("d1")
                .fileId(null)
                .build();

        when(repository.findById("d1")).thenReturn(Optional.of(document));

        assertThatThrownBy(() -> service.download("d1"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}

