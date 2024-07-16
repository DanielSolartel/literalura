package com.alura.literalura.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "writers")
public class Writer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;
    @Column(unique = true)
    private String fullName;
    private Integer birthYear;
    private Integer deathYear;
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Novel> novels;

    public Writer() {
    }

    // Constructor for Writer with parameter WriterData
    public Writer(WriterData writerData){
        this.fullName = writerData.fullName();
        this.birthYear = writerData.birthYear();
        this.deathYear = writerData.deathYear();
    }

    // Getter and Setter
    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Novel> getNovels() {
        return novels;
    }

    public void setNovels(List<Novel> novels) {
        novels.forEach(n -> n.setWriter(this));
        this.novels = novels;
    }

    // toString for Writer
    @Override
    public String toString() {
        return   "identifier=" + identifier +'\'' +
                ", FullName='" + fullName + '\'' +
                ", Birth Year=" + birthYear +'\'' +
                ", Death Year=" + deathYear;
    }
}
