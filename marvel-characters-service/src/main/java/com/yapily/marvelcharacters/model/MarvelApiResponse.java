package com.yapily.marvelcharacters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents the response structure of a GET Marvel Character by ID and GET all Characters
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelApiResponse {

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
        private Integer id;
        private String name;
        private String description;
        private CharacterThumbnail thumbnail;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public CharacterThumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(CharacterThumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }

        @NotNull
        public static class CharacterThumbnail {
            private String path;
            private String extension;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }
        }
    }
}