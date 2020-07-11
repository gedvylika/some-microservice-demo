package com.example.homework.repository;
import com.example.homework.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
    List<Artist> findByArtistNameContainingIgnoreCase(String name);
    List<Artist> findAll();
}