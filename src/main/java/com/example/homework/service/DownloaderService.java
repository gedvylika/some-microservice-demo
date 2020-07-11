package com.example.homework.service;

import be.ceau.itunesapi.Lookup;
import be.ceau.itunesapi.Search;
import be.ceau.itunesapi.request.Entity;
import be.ceau.itunesapi.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DownloaderService {

    public Response getArtistsList(String name) {
        return new Search(name)
                .setEntity(Entity.ALL_ARTIST)
                .execute();
    }

    public Response getAlbumList(Long artistId) {
        return new Lookup()
                .addId(String.valueOf(artistId))
                .setEntity(Entity.ALBUM)
                .setLimit(5)
                .execute();
    }

}
