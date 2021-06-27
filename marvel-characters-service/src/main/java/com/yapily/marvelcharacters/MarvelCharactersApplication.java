package com.yapily.marvelcharacters;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@SpringBootApplication
public class MarvelCharactersApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MarvelCharactersApplication.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        // Paginate through the list of marvel characters and store the IDs
        SpringApplication.run(MarvelCharactersApplication.class, args);
    }

}
