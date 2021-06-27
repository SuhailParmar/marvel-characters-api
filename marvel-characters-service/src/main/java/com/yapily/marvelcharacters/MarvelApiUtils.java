package com.yapily.marvelcharacters;

import com.yapily.marvelcharacters.model.MarvelApiResponse;
import com.yapily.marvelcharacters.models.Character;
import com.yapily.marvelcharacters.models.CharacterThumbnail;

import java.util.List;
import java.util.stream.Collectors;

public class MarvelApiUtils {

    /**
     * Convert the get all characters response into a list of character ids
     * @param apiResponse the response from the Marvel API
     * @return the list of character ids
     */
    public static List<Integer> convertResponseIntoListOfCharacterIds(MarvelApiResponse apiResponse){
        return apiResponse.getData().getResults()
                .parallelStream()
                .map(MarvelApiResponse.CharacterRecord::getId)
                .collect(Collectors.toList());
    }

    /**
     * Convert the response from the Marvel API into the Character model.
     * By decoupling our Character model from GetAllCharactersResponse.CharacterRecord we ensure that any change
     * made to the Marvel API does not affect our model.
     * @param response - The get single character response from the marvel API
     * @return A reduced model of that character
     */
    public static Character decorator(MarvelApiResponse response){
        MarvelApiResponse.CharacterRecord record = response.getData().getResults().get(0);
        Character character = new Character();
        character.setDescription(record.getDescription());
        character.setId((int) record.getId());
        character.setName(record.getName());
        CharacterThumbnail thumbnail = new CharacterThumbnail();
        thumbnail.setExtension(record.getThumbnail().getExtension());
        thumbnail.setPath(record.getThumbnail().getPath());
        character.setThumbnail(thumbnail);
        return character;
    }
}
