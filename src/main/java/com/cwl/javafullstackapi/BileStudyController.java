package com.cwl.javafullstackapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class BileStudyController {

    @Autowired
    private BibleStudyService bibleStudyService;
    @GetMapping("/bible-topics")
    public String callSecondApi() {
        String apiUrl = "https://iq-bible.p.rapidapi.com/GetTopics";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", "9d752015b3msh447c523c9c6318bp16283fjsn3fb61e8b3e4d");
        headers.set("X-Rapidapi-Host", "iq-bible.p.rapidapi.com");
        // Set up headers for the second API if needed

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
    @GetMapping("/bible-plan")
    public String allBibleStudy(){

        String apiUrl = "https://iq-bible.p.rapidapi.com/GetTopics";

        // Set up request headers including RapidAPI key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", "9d752015b3msh447c523c9c6318bp16283fjsn3fb61e8b3e4d");
        headers.set("X-Rapidapi-Host", "iq-bible.p.rapidapi.com");

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Make the request with the headers
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // Return the response body
        return response.getBody();
    }

    @GetMapping("/bible-verseId")
    public String verseId(String vId){

        String apiUrl = "https://iq-bible.p.rapidapi.com/GetVerse?verseId=" + vId;

        // Set up request headers including RapidAPI key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", "9d752015b3msh447c523c9c6318bp16283fjsn3fb61e8b3e4d");
        headers.set("X-Rapidapi-Host", "iq-bible.p.rapidapi.com");

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Make the request with the headers
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // Return the response body
        return response.getBody();
    }

}
