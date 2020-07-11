package com.example.homework.repository;
import com.example.homework.model.Album;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, Long> {
    List<Album> findByArtistId(Long artistId);
    List<Album> findAll();
}