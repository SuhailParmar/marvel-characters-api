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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@RestController
public class MarvelCharactersController implements CharactersApi {

    private static final Logger log = LoggerFactory.getLogger(MarvelCharactersController.class);

    @Autowired
    MarvelApiService marvelApiService;

    @Override
    @Cacheable(value = "marvel")
    @EventListener(ApplicationReadyEvent.class)
    public ResponseEntity<List<Integer>> getAllCharacters() {
        final int limit = 100;

        // We need to make an initial api request to retrieve the total number of marvel characters
        MarvelApiResponse response = marvelApiService.getAllMarvelCharacters(limit, 0);

        float totalNumberOfCharacters = response.getData().getTotal(); // Kept as float for floating point division.
        float numberOfIterationsAsFloat = (totalNumberOfCharacters / limit) - 1; // remove the re-iterating the first set
        final int numberOfIterations = (int) Math.ceil(numberOfIterationsAsFloat);

        final List<Integer> marvelCharacterIds = new ArrayList<>(
                MarvelApiUtils.convertResponseIntoListOfCharacterIds(response));

        IntStream.range(1, numberOfIterations + 1)
                .parallel()
                .mapToObj(iteration -> {
                    int offset = iteration * 100; // Calculate offset to prevent retrieving the same ids
                    MarvelApiResponse r = marvelApiService.getAllMarvelCharacters(100, offset);
                    return MarvelApiUtils.convertResponseIntoListOfCharacterIds(r);
                }).flatMap(Collection::stream) // flatten the stream of lists into a single list of ints
                .forEachOrdered(marvelCharacterIds::add);

        log.info("Retrieved {} character ids! Caching results.", marvelCharacterIds.size());
        return new ResponseEntity<>(marvelCharacterIds, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Character> getOneCharacter(Integer id) {
        if (id < 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            MarvelApiResponse response = marvelApiService.getMarvelCharacterById(id);
            return new ResponseEntity<>(MarvelApiUtils.decorator(response), HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            // If I had more time I would have learnt more about SpringBoot error handling
            if (e.getResponseBodyAsString().contains("We couldn't find that character")) {
                log.warn("Marvel character with ID {} could not be found.", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}