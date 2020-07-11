package com.example.homework.repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
    List<Artist> findByArtistNameContainingIgnoreCase(String name);
    List<Artist> findAll();
}