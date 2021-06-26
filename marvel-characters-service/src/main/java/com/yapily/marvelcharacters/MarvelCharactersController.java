package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.api.CharactersApi;
import com.yapily.marvelcharacters.models.Character;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class MarvelCharactersController implements CharactersApi {

    @Override
    public ResponseEntity<List<Character>> getAllCharacters() {
        return null;
    }

}
