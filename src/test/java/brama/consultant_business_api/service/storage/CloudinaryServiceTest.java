package brama.consultant_business_api.service.storage;

import brama.consultant_business_api.config.CloudinaryProperties;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.mock.web.MockMultipartFile;
import brama.consultant_business_api.support.TestUploadFile;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {
    @Mock
    private ObjectProvider<Cloudinary> cloudinaryProvider;
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private Uploader uploader;
    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private CloudinaryService service;

    @BeforeEach
    void setUp() {
        CloudinaryProperties properties = new CloudinaryProperties();
        properties.setEnabled(true);
        service = new CloudinaryService(cloudinaryProvider, properties, restTemplateBuilder);
    }

    @Test
    void uploadUsesCloudinaryAndReturnsResult() throws Exception {
        TestUploadFile.assumeExists();
        when(cloudinaryProvider.getIfAvailable()).thenReturn(cloudinary);
        when(cloudinary.uploader()).thenReturn(uploader);

        Map<String, Object> result = new HashMap<>();
        result.put("public_id", "public-123");
        result.put("secure_url", "https://cloudinary.example/file");
        result.put("resource_type", "image");

        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(result);

        MockMultipartFile file = TestUploadFile.mockMultipartFile();

        CloudinaryService.CloudinaryUploadResult uploaded = service.upload(file, "documents/p1");

        assertThat(uploaded.publicId()).isEqualTo("public-123");
        assertThat(uploaded.secureUrl()).isEqualTo("https://cloudinary.example/file");
        assertThat(uploaded.resourceType()).isEqualTo("image");

        verify(uploader).upload(any(byte[].class), argThat(options -> "documents/p1".equals(options.get("folder"))));
    }

    @Test
    void uploadWithoutCloudinaryConfiguredThrows() {
        TestUploadFile.assumeExists();
        when(cloudinaryProvider.getIfAvailable()).thenReturn(null);
        MockMultipartFile file = TestUploadFile.mockMultipartFile();

        assertThatThrownBy(() -> service.upload(file, "documents/p1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cloudinary is not configured");
    }
}
