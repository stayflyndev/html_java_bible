package com.cwl.javafullstackapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reading")
public class BibleStudyController {

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/bible-topics")
    public String callSecondApi() {
        String apiUrl = "https://iq-bible.p.rapidapi.com/GetTopics";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", "");
        headers.set("X-Rapidapi-Host", "iq-bible.p.rapidapi.com");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    // Endpoint to fetch details for a specific topic
    @GetMapping("/get-topic-details")
    public ResponseEntity<List<TopicDetail>> getTopicDetails(@RequestParam String topic) {
        String apiUrl = "https://iq-bible.p.rapidapi.com/GetTopic?topic=" + topic;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", "");
        headers.set("X-Rapidapi-Host", "iq-bible.p.rapidapi.com");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        // Parse the JSON response from the API
        List<TopicDetail> topicDetails = parseTopicDetails(response.getBody());

        return ResponseEntity.ok(topicDetails);  // Return the details as a list of TopicDetail objects
    }

    // Helper method to parse the API response and extract the topic details
    private List<TopicDetail> parseTopicDetails(String responseBody) {
        List<TopicDetail> details = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            if (rootNode.isArray()) {
                for (JsonNode detailNode : rootNode) {
                    String citation = detailNode.path("citation").asText();
                    JsonNode verseIdsNode = detailNode.path("verseIds");

                    List<String> verseIds = new ArrayList<>();
                    if (verseIdsNode.isArray()) {
                        for (JsonNode verseIdNode : verseIdsNode) {
                            verseIds.add(verseIdNode.asText());
                        }
                    }
                    details.add(new TopicDetail(citation, verseIds));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            details.add(new TopicDetail("Error parsing response", new ArrayList<>()));
        }
        return details;
    }

    // DTO class to represent each topic's citation and verseIds
    public static class TopicDetail {
        private String citation;
        private List<String> verseIds;

        public TopicDetail(String citation, List<String> verseIds) {
            this.citation = citation;
            this.verseIds = verseIds;
        }

        public String getCitation() {
            return citation;
        }

        public List<String> getVerseIds() {
            return verseIds;
        }
    }
}
