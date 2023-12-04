package com.example.group4_final_project.services.implementations;

import com.example.group4_final_project.models.DTOs.WikiPageDto;
import com.example.group4_final_project.services.contracts.WikiService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class WikiServiceImpl implements WikiService {

    private final HttpClient httpClient;

    public WikiServiceImpl() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public List<WikiPageDto> searchWikipedia(String searchTerm) {
        try {
            String searchApiUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="
                    + URLEncoder.encode(searchTerm, StandardCharsets.UTF_8)
                    + "&utf8=&format=json&srlimit=5";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(searchApiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode searchResults = rootNode.path("query").path("search");

            List<WikiPageDto> wikiPageDtos = new ArrayList<>();
            if (searchResults.isArray()) {
                for (JsonNode result : searchResults) {
                    String title = result.path("title").asText();
                    String snippet = result.path("snippet").asText();
                    String plainSnippet = stripHtmlTags(snippet); // Strip HTML tags

                    // Construct the full URL using the title
                    String fullUrl = "https://en.wikipedia.org/wiki/" + URLEncoder.encode(title.replace(" ", "_"), StandardCharsets.UTF_8);

                    wikiPageDtos.add(new WikiPageDto(title, plainSnippet, fullUrl));
                }
            }

            return wikiPageDtos;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return new ArrayList<>();
        }
    }

    private String stripHtmlTags(String html) {
        return html.replaceAll("<[^>]*>", "");
    }
}
