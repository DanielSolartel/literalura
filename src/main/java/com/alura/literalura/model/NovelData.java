package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NovelData(@JsonAlias("id") Long identifier,
                        @JsonAlias("title") String name,
                        @JsonAlias("authors") List<WriterData> writers,
                        @JsonAlias("languages") List<String> tongues,
                        @JsonAlias("copyright") String rights,
                        @JsonAlias("download_count") Integer downloads) {

}

