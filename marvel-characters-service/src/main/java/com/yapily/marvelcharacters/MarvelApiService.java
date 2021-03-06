package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.model.MarvelApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.AbstractMap;

@Component
public class MarvelApiService {

    @Autowired
    public RestTemplate restTemplate;

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

    public MarvelApiResponse getAllMarvelCharacters(int limit, int offset) {
        AbstractMap.SimpleEntry<Long, String> timestampWithMd5 = MD5Utils.createTimestampsAndMd5Hash(privateKey, publicKey);

        URI uri = UriComponentsBuilder.fromUri(baseUrl)
                .path("/characters")
                .queryParam(API_KEY, publicKey)
                .queryParam(TIMESTAMP, timestampWithMd5.getKey())
                .queryParam(HASH, timestampWithMd5.getValue())
                .queryParam(LIMIT, limit)
                .queryParam(OFFSET, offset)
                .build()
                .toUri();

        MarvelApiResponse apiResponse = restTemplate.getForObject(uri, MarvelApiResponse.class);
        if (apiResponse == null) throw new RuntimeException("Failed to retrieve marvel characters. Aborting.");
        log.info("Successfully retrieved {} marvel characters from the MarvelAPI!", apiResponse.getData().getCount());
        return apiResponse;
    }

    public MarvelApiResponse getMarvelCharacterById(Integer id) {

        AbstractMap.SimpleEntry<Long, String> timestampWithMd5 = MD5Utils.createTimestampsAndMd5Hash(privateKey, publicKey);

        URI uri = UriComponentsBuilder.fromUri(baseUrl)
                .path("/characters/" + id)
                .queryParam(API_KEY, publicKey)
                .queryParam(TIMESTAMP, timestampWithMd5.getKey())
                .queryParam(HASH, timestampWithMd5.getValue())
                .build()
                .toUri();

        log.info("Retrieving Marvel character with ID of {}", id);
        return restTemplate.getForObject(uri, MarvelApiResponse.class);
    }
}
