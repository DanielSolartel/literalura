package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record WriterData(@JsonAlias("name") String fullName,
                         @JsonAlias("birth_year") Integer birthYear,
                         @JsonAlias("death_year") Integer deathYear) {
}