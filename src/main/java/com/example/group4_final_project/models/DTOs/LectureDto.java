package com.example.group4_final_project.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class LectureDto {
    private Integer id;
    private Integer courseId;
    @NotNull
    @Size(min = 5, max = 50)
    private String title;
    @URL
    private String videoLink;
    @Size(max = 1000)
    private String description;

    private AssignmentDto assignment;

    public void setVideoLink(String videoLink) {
        this.videoLink = convertToEmbedURL(videoLink);
    }

    private String convertToEmbedURL(String url) {
        // Logic to convert the standard YouTube URL to an embed URL
        // Example: Convert https://www.youtube.com/watch?v=videoId
        // to https://www.youtube.com/embed/videoId
        if (url != null && url.contains("watch?v=")) {
            return url.replace("watch?v=", "embed/");
        }
        return url;
    }
}
