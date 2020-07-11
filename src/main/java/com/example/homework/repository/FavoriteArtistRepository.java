package com.example.homework.repository;
import com.example.homework.model.FavoriteArtist;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteArtistRepository extends CrudRepository<FavoriteArtist, Long> {
    FavoriteArtist getByUserIdAndArtistId(Long userId, Long artistId);
}