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

        List<TopicDetail> topicDetails = parseTopicDetails(response.getBody());

        // Fetch verses for each topic detail based on the verseIds
        for (TopicDetail topicDetail : topicDetails) {
            List<Verse> verses = new ArrayList<>();
            for (String verseId : topicDetail.getVerseIds()) {
                Verse verse = getVerseDetails(verseId);
                verses.add(verse);
            }
            topicDetail.setVerses(verses);  // Set the verses to the topic detail
        }

        return ResponseEntity.ok(topicDetails);  // Return the details as a list of TopicDetail objects
    }

    private Verse getVerseDetails(String verseId) {
        String apiUrl = "https://iq-bible.p.rapidapi.com/GetVerse?verseId=" + verseId + "&versionId=bbe";

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

        // Parse the verse details from the response
        return parseVerseDetails(response.getBody());
    }

    // Helper method to parse the API response for verse details
    private Verse parseVerseDetails(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Ensure the root is an array and contains at least one verse
            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode verseNode = rootNode.get(0);  // Getting the first (and possibly only) verse in the array

                // Extract verse details from the response
                String book = verseNode.path("b").asText();   // Book number
                String chapter = verseNode.path("c").asText(); // Chapter number
                String verse = verseNode.path("v").asText();   // Verse number
                String text = verseNode.path("t").asText();    // Verse text

                // Construct the reference (Book Chapter:Verse)
                String reference = "Book " + book + " " + chapter + ":" + verse;

                // Return a new Verse object
                return new Verse(reference, text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Fallback in case of errors
        return new Verse("Error", "Could not fetch verse details.");
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
        private List<Verse> verses;  // Added list of verses

        public TopicDetail(String citation, List<String> verseIds) {
            this.citation = citation;
            this.verseIds = verseIds;
            this.verses = new ArrayList<>();
        }

        public String getCitation() {
            return citation;
        }

        public List<String> getVerseIds() {
            return verseIds;
        }

        public List<Verse> getVerses() {
            return verses;
        }

        public void setVerses(List<Verse> verses) {
            this.verses = verses;
        }
    }

    // DTO class for Verse details
    public static class Verse {
        private String reference;
        private String text;

        public Verse(String reference, String text) {
            this.reference = reference;
            this.text = text;
        }

        public String getReference() {
            return reference;
        }

        public String getText() {
            return text;
        }
    }

}
