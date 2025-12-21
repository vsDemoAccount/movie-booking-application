package movies.userservice.serviceimpl.S3FileStorageService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.userservice.service.FileStorageService.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String folderPath) {
        // Generate unique filename
        String fileName = folderPath + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Assuming standard AWS S3 URL format
            // If you use a custom region or endpoint, adjust this string
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        } catch (IOException e) {
            log.error("Failed to upload file to S3", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        // Implementation pending
    }
}