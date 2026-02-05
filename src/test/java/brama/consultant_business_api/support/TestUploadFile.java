package brama.consultant_business_api.support;

import org.junit.jupiter.api.Assumptions;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestUploadFile {
    public static final Path WEBP_PATH = Path.of("C:\\Users\\arebh\\Desktop\\New folder\\1600w-a1RYzvS1EFo.webp");

    private TestUploadFile() {
    }

    public static void assumeExists() {
        Assumptions.assumeTrue(Files.exists(WEBP_PATH), "Upload test file is missing");
    }

    public static MockMultipartFile mockMultipartFile() {
        return new MockMultipartFile("file", "1600w-a1RYzvS1EFo.webp", "image/webp", readBytes());
    }

    public static FileSystemResource fileSystemResource() {
        return new FileSystemResource(WEBP_PATH.toFile());
    }

    private static byte[] readBytes() {
        try {
            return Files.readAllBytes(WEBP_PATH);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read upload test file", ex);
        }
    }
}
