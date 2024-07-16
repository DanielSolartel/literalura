package com.alura.literalura.repository;


import com.alura.literalura.model.Novel;
import com.alura.literalura.model.Tongue;
import com.alura.literalura.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Writer, Long> {

    Optional<Writer> findWriterByNameContaining(String name);

    @Query("SELECT b FROM Book b JOIN b.author a WHERE b.title LIKE %:bTitle%")
    Optional<Novel> getNovelContainsEqualsIgnoreCaseTitle(String bTitle);

    @Query("SELECT b FROM Author a JOIN a.books b")
    List<Novel> findNovelsByWriter();

    @Query("SELECT a FROM Author a WHERE a.dateOfDecease > :date")
    List<Writer> getWriterbyDateOfDecease(Integer date);

    @Query("SELECT b FROM Author a JOIN a.books b WHERE b.language = :language")
    List<Novel> findNovelByTongue(Tongue tongue);

}
