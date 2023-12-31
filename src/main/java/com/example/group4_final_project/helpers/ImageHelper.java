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
public class ImageHelper {
    private static final String CLOUD_NAME = "dnyhome4y";
    private static final String API_KEY = "619467576556645";
    private static final String API_SECRET = "OmEQnSsEgC3ZHdDZQkxGR2BeVwA";

    private final Cloudinary cloudinary;
    /*@Value("${}")
    private  String test;*/


    public ImageHelper() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }

    public String uploadImage(MultipartFile image) throws IOException {
        File tempFile = File.createTempFile("tempImage", ".jpg");
        image.transferTo(tempFile);

        Map uploadResult = cloudinary.uploader().upload(tempFile,
                ObjectUtils.asMap(
                        "folder", "user-image"
                ));

        tempFile.deleteOnExit();

        return uploadResult.get("url").toString();
    }
}
