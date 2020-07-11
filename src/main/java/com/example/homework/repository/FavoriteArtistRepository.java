package com.example.homework.repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteArtistRepository extends CrudRepository<FavoriteArtist, Long> {
    FavoriteArtist getByUserIdAndArtistId(Long userId, Long artistId);
}