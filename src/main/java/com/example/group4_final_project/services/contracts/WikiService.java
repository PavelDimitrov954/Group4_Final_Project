package com.example.group4_final_project.services.contracts;

import com.example.group4_final_project.models.DTOs.WikiPageDto;

import java.util.List;

public interface WikiService {

    List<WikiPageDto> searchWikipedia(String searchTerm);
}
