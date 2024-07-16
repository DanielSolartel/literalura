package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "novels")
public class Novel {
    @Id
    private Long identifier;
    private String name;
    @Enumerated(EnumType.STRING)
    private Tongue tongue;
    private String rights;
    private Integer downloads;
    @ManyToOne
    private Writer writer;

    public Novel() {
    }

    public Novel(NovelData novelData){
        this.identifier = novelData.identifier();
        this.name = novelData.name();
        this.tongue = tongue.fromString(novelData.tongues().stream()
                .limit(1).collect(Collectors.joining()));
        this.rights = novelData.rights();
        this.downloads = novelData.downloads();
    }

    // Getters and Setters
    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tongue getTongue() {
        return tongue;
    }

    public void setTongue(Tongue tongue) {
        this.tongue = tongue;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return
                "identifier=" + identifier +'\'' +
                        ", Name='" + name + '\'' +
                        ", Tongue=" + tongue +
                        ", Rights='" + rights + '\'' +
                        ", Downloads=" + downloads +'\'' +
                        ", Writer=" + writer;
    }
}
