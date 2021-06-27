package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.api.CharactersApi;
import com.yapily.marvelcharacters.model.MarvelApiResponse;
import com.yapily.marvelcharacters.models.Character;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MarvelCharactersController implements CharactersApi {

    private static final Logger log = LoggerFactory.getLogger(MarvelCharactersController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MarvelApiService marvelApiService;

    @Override
    @Cacheable(value = "marvel")
    @EventListener(ApplicationReadyEvent.class)
    public ResponseEntity<List<Integer>> getAllCharacters() {
        List<Integer> marvelUserIds = new ArrayList<>();

        int offset = 0; // Counted Records So far
        int totalCharactersToCount = 1; // initial value

        MarvelApiResponse response;
        while (totalCharactersToCount > 0) {
            response = marvelApiService.getAllMarvelCharacters(restTemplate, 100, offset);
            offset += 100;
            marvelUserIds.addAll(MarvelApiUtils.convertResponseIntoListOfCharacterIds(response));
            totalCharactersToCount = 0; // response.getData().getTotal() - offset;
        }

        log.info("Retrieved {} character ids!", marvelUserIds.size());
        // Return all the IDs
        return new ResponseEntity<>(marvelUserIds, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Character> getOneCharacter(Integer id) {
        if (id <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            MarvelApiResponse response = marvelApiService.getMarvelCharacterById(restTemplate, id);
            return new ResponseEntity<>(MarvelApiUtils.decorator(response), HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            if (e.getResponseBodyAsString().contains("We couldn't find that character")) {
                log.warn("Marvel character with ID {} could not be found.", id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}