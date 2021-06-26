package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.model.ApiKeys;
import com.yapily.marvelcharacters.model.GetAllCharactersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarvelApiService {

    private static final Logger log = LoggerFactory.getLogger(MarvelCharactersApplication.class);

    private final String baseUrl;

    public MarvelApiService() {
        // Could move to application.properties if there was a different baseUrl per environment
        final String protocol = "https";
        final String host = "gateway.marvel.com";
        final String apiVersion = "v1";
        final String access = "public";
        baseUrl = String.format("%s://%s/%s/%s", protocol, host, apiVersion, access);
    }

    // Query Params required for the Marvel API
    private static final String API_KEY = "apikey";
    private static final String TIMESTAMP = "ts";
    private static final String HASH = "hash";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    // TODO move to environment variable
    private final String privateKey = ApiKeys.PRIVATE_KEY;
    private final String publicKey = ApiKeys.PUBLIC_KEY;

    public GetAllCharactersResponse getAllMarvelCharacters(RestTemplate restTemplate, String ts, int limit, int offset) {

        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/characters")
                .queryParam(API_KEY, publicKey)
                .queryParam(TIMESTAMP, ts)
                .queryParam(HASH, MD5Utils.digest(ts, privateKey, publicKey))
                .queryParam(LIMIT, limit)
                .queryParam(OFFSET, offset)
                .build()
                .toUri();

        GetAllCharactersResponse r = restTemplate.getForObject(uri, GetAllCharactersResponse.class);
        if(r == null) throw new RuntimeException("Failed to retrieve marvel characters. Aborting.");
        log.info("Successfully retrieved {} marvel characters from the MarvelAPI!", r.getData().getCount());
        return r;
    }

    // TODO unique-ify TS
    public List<String> getAllMarvelUserIds(RestTemplate restTemplate, String ts) {
        List<String> marvelUserIds = new ArrayList<>();

        int offset = 0; // Counted Records So far
        int totalCharactersToCount = 1; // inital value

        GetAllCharactersResponse response;
        while(totalCharactersToCount > 0){
            response = getAllMarvelCharacters(restTemplate, ts, 100, offset);
            offset += 100;
            marvelUserIds.addAll(convertResponseIntoStringList(response));
            totalCharactersToCount = response.getData().getTotal() - offset;
        }

        log.info("Successfully returned {} ids!", marvelUserIds.size());
        return marvelUserIds;
    }

    // Could improve? We could stream all records then convert to list?
    private List<String> convertResponseIntoStringList(GetAllCharactersResponse r){
        return r.getData().getResults()
                .parallelStream()
                .map(GetAllCharactersResponse.CharacterRecord::getId)
                .collect(Collectors.toList());
    }
}
