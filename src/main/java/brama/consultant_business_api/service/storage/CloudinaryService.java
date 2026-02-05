package brama.consultant_business_api.service.storage;

import brama.consultant_business_api.config.CloudinaryProperties;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final ObjectProvider<Cloudinary> cloudinaryProvider;
    private final CloudinaryProperties properties;
    private final RestTemplateBuilder restTemplateBuilder;

    public boolean isEnabled() {
        return properties.isEnabled();
    }

    public CloudinaryUploadResult upload(final MultipartFile file, final String folder) {
        final Cloudinary cloudinary = requireCloudinary();
        try {
            final Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "auto",
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false
            );
            @SuppressWarnings("unchecked")
            final Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), options);
            return new CloudinaryUploadResult(
                    (String) result.get("public_id"),
                    (String) result.get("secure_url"),
                    (String) result.get("resource_type")
            );
        } catch (Exception ex) {
            throw new RuntimeException("Cloudinary upload failed", ex);
        }
    }

    public byte[] download(final String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }
        final RestTemplate restTemplate = restTemplateBuilder.build();
        final ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        return response.getBody();
    }

    public void delete(final String publicId, final String resourceType) {
        if (!StringUtils.hasText(publicId)) {
            return;
        }
        final Cloudinary cloudinary = requireCloudinary();
        try {
            final String type = StringUtils.hasText(resourceType) ? resourceType : "raw";
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", type));
        } catch (Exception ex) {
            throw new RuntimeException("Cloudinary delete failed", ex);
        }
    }

    private Cloudinary requireCloudinary() {
        final Cloudinary cloudinary = cloudinaryProvider.getIfAvailable();
        if (cloudinary == null) {
            throw new IllegalStateException("Cloudinary is not configured");
        }
        return cloudinary;
    }

    public record CloudinaryUploadResult(String publicId, String secureUrl, String resourceType) {
    }
}
