package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.model.MarvelApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class MarvelApiService {

    private static final Logger log = LoggerFactory.getLogger(MarvelApiService.class);

    private final URI baseUrl;
    // Query Params required for the Marvel API
    private static final String API_KEY = "apikey";
    private static final String TIMESTAMP = "ts";
    private static final String HASH = "hash";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    @Value("${key.private}")
    private String privateKey;

    @Value("${key.public}")
    private String publicKey;

    public MarvelApiService() {
        UriComponents uriComponents = UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("gateway.marvel.com")
                .pathSegment("v1")
                .pathSegment("public")
                .build();

        baseUrl = uriComponents.toUri();
    }

    public MarvelApiResponse getAllMarvelCharacters(RestTemplate restTemplate, String ts, int limit, int offset) {

        URI uri = UriComponentsBuilder.fromUri(baseUrl)
                .path("/characters")
                .queryParam(API_KEY, publicKey)
                .queryParam(TIMESTAMP, ts)
                .queryParam(HASH, MD5Utils.digest(ts, privateKey, publicKey))
                .queryParam(LIMIT, limit)
                .queryParam(OFFSET, offset)
                .build()
                .toUri();

        MarvelApiResponse apiResponse = restTemplate.getForObject(uri, MarvelApiResponse.class);
        if (apiResponse == null) throw new RuntimeException("Failed to retrieve marvel characters. Aborting.");
        log.info("Successfully retrieved {} marvel characters from the MarvelAPI!", apiResponse.getData().getCount());
        return apiResponse;
    }

    // TODO unique-ify TS
    public List<Integer> getAllMarvelUserIds(RestTemplate restTemplate, String ts) {
        List<Integer> marvelUserIds = new ArrayList<>();

        int offset = 0; // Counted Records So far
        int totalCharactersToCount = 1; // initial value

        MarvelApiResponse response;
        while (totalCharactersToCount > 0) {
            response = getAllMarvelCharacters(restTemplate, ts, 100, offset);
            offset += 100;
            marvelUserIds.addAll(MarvelApiUtils.convertResponseIntoListOfCharacterIds(response));
            totalCharactersToCount = response.getData().getTotal() - offset;
        }

        log.info("Retrieved {} character ids!", marvelUserIds.size());
        return marvelUserIds;
    }

    public MarvelApiResponse getMarvelCharacterById(RestTemplate restTemplate, Integer id, String ts) {
        URI uri = UriComponentsBuilder.fromUri(baseUrl)
                .path("/characters/" + id)
                .queryParam(API_KEY, publicKey)
                .queryParam(TIMESTAMP, ts)
                .queryParam(HASH, MD5Utils.digest(ts, privateKey, publicKey))
                .build()
                .toUri();

        log.info("Retrieving Marvel character with ID of {}", id);
        return restTemplate.getForObject(uri, MarvelApiResponse.class);
    }
}
