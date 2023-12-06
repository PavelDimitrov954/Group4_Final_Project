package com.example.group4_final_project.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class AssignmentHelper {

    private final Cloudinary cloudinary;

    public AssignmentHelper(@Value("${cloudinary.cloud-name}") String cloudName,
                            @Value("${cloudinary.api-key}") String apiKey,
                            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadAssignment(MultipartFile assignmentFile) throws IOException {
        File tempFile = File.createTempFile("tempAssignment", ".txt");
        assignmentFile.transferTo(tempFile);

        Map uploadResult = cloudinary.uploader().upload(tempFile,
                ObjectUtils.asMap(
                        "folder", "files",
                        "resource_type", "raw" // Specify that the file is a raw, unprocessed file, suitable for text files
                ));

        tempFile.deleteOnExit();

        return uploadResult.get("url").toString();
    }
}
