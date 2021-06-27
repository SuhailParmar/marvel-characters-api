package com.yapily.marvelcharacters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapily.marvelcharacters.model.MarvelApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class UtilsTest {

    @Test
    void testMd5Digest() {
        String d = MD5Utils.createDigest("hello", "world", "hello");
        // 32 character String is generated
        Assertions.assertEquals("04a3d22cc58005ae60e84985a6f6c557", d);
    }

    @Test
    void testConvertResponseIntoListOfCharacterIds() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("getAllCharactersResponse.json");
        ObjectMapper mapper = new ObjectMapper();

        // Givem
        MarvelApiResponse response = mapper.readValue(is, MarvelApiResponse.class);

        // When
        List<Integer> ids = MarvelApiUtils.convertResponseIntoListOfCharacterIds(response);

        // Then
        Assertions.assertEquals(List.of(1011334, 1017100, 1009144), ids);
    }
}