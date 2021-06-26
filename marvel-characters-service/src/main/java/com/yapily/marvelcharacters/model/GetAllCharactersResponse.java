package com.yapily.marvelcharacters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.List;

/*
 * Represents the response model of the MarvelAPI GET Characters
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllCharactersResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NotNull
    public static class Data {
        private List<CharacterRecord> results;
        private int total;
        private int count;
        private int offset;

        public List<CharacterRecord> getResults() {
            return results;
        }

        public void setResults(List<CharacterRecord> results) {
            this.results = results;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    @NotNull
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CharacterRecord {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}