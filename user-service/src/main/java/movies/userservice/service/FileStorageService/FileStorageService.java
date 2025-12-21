package movies.userservice.service.FileStorageService;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /**
     * Uploads file to cloud storage and returns the public URL.
     */
    String uploadFile(MultipartFile file, String folderPath);

    /**
     * Deletes file from cloud storage.
     */
    void deleteFile(String fileUrl);
}