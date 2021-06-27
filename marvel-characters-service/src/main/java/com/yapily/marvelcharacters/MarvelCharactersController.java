package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.api.CharactersApi;
import com.yapily.marvelcharacters.model.MarvelApiResponse;
import com.yapily.marvelcharacters.models.Character;
import com.yapily.marvelcharacters.models.CharacterThumbnail;
import io.swagger.models.auth.In;
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
    public ResponseEntity<List<Integer>> getAllCharacters() {
        // Return all the IDs
        List<Integer> marvelUserIds = marvelApiService.getAllMarvelUserIds(restTemplate);
        return new ResponseEntity<>(marvelUserIds, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Character> getOneCharacter(Integer id) {
        if(id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        MarvelApiResponse response = marvelApiService.getMarvelCharacterById(restTemplate, id);
        return new ResponseEntity<>(MarvelApiUtils.decorator(response), HttpStatus.OK);
    }
}