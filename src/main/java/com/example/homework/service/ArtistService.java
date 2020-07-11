package com.example.homework.service;

import be.ceau.itunesapi.response.Result;
import com.example.homework.model.Album;
import com.example.homework.model.Artist;
import com.example.homework.model.FavoriteArtist;
import com.example.homework.model.LastSearch;
import com.example.homework.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class ArtistService {

    private static final int CACHE_VALIDITY_IN_DAYS = 1;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    FavoriteArtistRepository favoriteArtistRepository;

    @Autowired
    DownloaderService downloaderService;

    @Autowired
    LastSearchRepository lastArtistSearchRepository;

    private boolean cacheIsTooOld(Date checkDate) {
        var cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, CACHE_VALIDITY_IN_DAYS * -1);
        return checkDate.before(cal.getTime());
    }

    public List<Artist> searchArtists(String name) {
        var lastSearch = lastArtistSearchRepository.findBySearchString(name);
        if (lastSearch == null || cacheIsTooOld(lastSearch.getTimestamp())) {
            return searchInRemoteAndCache(name);
        }
        return artistRepository.findByArtistNameContainingIgnoreCase(name);
    }

    private  List<Artist> searchInRemoteAndCache(String name) {
        List<Artist> artistList = new ArrayList<>();
        var downloadedArtists = downloaderService.getArtistsList(name);
        for(Result itunesArtist: downloadedArtists.getResults()) {
            var artist = Artist.builder()
                    .artistId(itunesArtist.getArtistId())
                    .artistName(itunesArtist.getArtistName())
                    .artistLinkUrl(itunesArtist.getArtistViewUrl())
                    .primaryGenreId(itunesArtist.getPrimaryGenreId())
                    .primaryGenreName(itunesArtist.getPrimaryGenreName())
                .build();
            artistList.add(artist);
            artistRepository.save(artist);
            lastArtistSearchRepository.save(LastSearch.builder()
                    .searchString(name)
                    .timestamp(Calendar.getInstance().getTime())
                    .build()
            );
        }
        return artistList;
    }

    public FavoriteArtist makeArtistFavorite(Long artistId, Long userId) {
        var favoriteInDB = favoriteArtistRepository.getByUserIdAndArtistId(userId, artistId);
        if (favoriteInDB != null) // Already there
            return favoriteInDB;

        var favoriteArtist = FavoriteArtist.builder()
                .artistId(artistId)
                .userId(userId)
            .build();
        getAndCacheTopFiveAlbums(artistId);
        return favoriteArtistRepository.save(favoriteArtist);
    }

    private List<Album> getAndCacheTopFiveAlbums(Long artistId) {
        var downloadedAlbums = downloaderService.getAlbumList(artistId);
        List<Album> albumList = new ArrayList<>();
        for (Result itunesAlbum: downloadedAlbums.getResults()) {
            if (itunesAlbum.getCollectionName() != null) {
                // Is this even album?
                var album = Album.builder()
                        .artistId(artistId)
                        .artistLinkUrl(itunesAlbum.getArtistViewUrl())
                        .artistName(itunesAlbum.getArtistName())
                        .primaryGenreId(itunesAlbum.getPrimaryGenreId())
                        .primaryGenreName(itunesAlbum.getPrimaryGenreName())
                        .albumName(itunesAlbum.getCollectionName())
                        .cachedDate(Calendar.getInstance().getTime())
                        .build();
                albumList.add(album);
                albumRepository.save(album);
            }
        }
        return albumList;
    }

    public List<Album> getArtistTop5Albums(Long artistId, Long userId) {
        if (favoriteArtistRepository.getByUserIdAndArtistId(userId, artistId) == null)
            return null; // Not a favorite

        var cachedAlbums = albumRepository.findByArtistId(artistId);

        // If there's nothing in cache OR things in cache too old
        if (cachedAlbums == null || (cachedAlbums.size() > 0 && cacheIsTooOld(cachedAlbums.get(0).getCachedDate())))
            return getAndCacheTopFiveAlbums(artistId); // Get fresh list and renew the cache

        return cachedAlbums;
    }
}
