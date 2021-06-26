package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.api.CharactersApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MarvelCharactersController implements CharactersApi {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MarvelApiService marvelApiService;

    @Override
    @Cacheable(value = "marvel")
    @EventListener(ApplicationReadyEvent.class)
    public ResponseEntity<List<String>> getAllCharacters() {
        // Return all the IDs
        List<String> marvelUserIds = marvelApiService.getAllMarvelUserIds(restTemplate, "helloworld");
        return new ResponseEntity<>(marvelUserIds, HttpStatus.OK);
    }
}