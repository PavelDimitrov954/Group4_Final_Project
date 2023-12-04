package com.example.group4_final_project.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WikiPageDto {
    private String title;
    private String snippet; // Short snippet
    private String url; // Full URL to the Wikipedia article
}
