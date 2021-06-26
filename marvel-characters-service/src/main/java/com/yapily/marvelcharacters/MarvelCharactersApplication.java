package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.model.GetAllCharactersResponse;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class MarvelCharactersApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MarvelCharactersApplication.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MarvelApiService marvelApiService() {
        return new MarvelApiService();
    }

    //@Bean
    //@EventListener(ApplicationReadyEvent.class)
    //public List<String> cacheAllIdsOnStartup() {
    //    String ts = "helloworld";
    //    return charactersController().getAllCharacters().getBody();
    //}

    public static void main(String[] args) {
        // Paginate through the list of marvel characters and store the IDs
        SpringApplication.run(MarvelCharactersApplication.class, args);
    }

}
