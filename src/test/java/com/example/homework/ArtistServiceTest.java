package com.example.homework;

import be.ceau.itunesapi.response.Response;
import be.ceau.itunesapi.response.Result;
import com.example.homework.model.Artist;
import com.example.homework.model.LastSearch;
import com.example.homework.repository.*;
import com.example.homework.service.ArtistService;
import com.example.homework.service.DownloaderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.Calendar;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArtistServiceTest {
    @Autowired
    private ArtistService artistService;

    @MockBean
    AlbumRepository albumRepository;

    @MockBean
    FavoriteArtistRepository favoriteArtistRepository;

    @MockBean
    private LastSearchRepository lastSearchRepository;

    @MockBean
    private DownloaderService downloaderService;

    @MockBean
    private ArtistRepository artistRepository;

    @Test
    public void oldSearchShouldDownload() {
        // Setup:
        final String TOO_OLD_SEARCH = "TooOldSearch";

        var oldTime = Calendar.getInstance();
        oldTime.add(Calendar.DAY_OF_YEAR, -2);

        Mockito.when(lastSearchRepository.findBySearchString(TOO_OLD_SEARCH))
                .thenReturn(new LastSearch(TOO_OLD_SEARCH, oldTime.getTime()));

        var mockResponse = new Response();
        var mockResultList = new ArrayList<Result>();
        var mockResult = new Result();
        mockResult.setArtistId(111L);
        mockResult.setArtistName("Response from ITunes definitely");
        mockResultList.add(mockResult);
        mockResponse.setResults(mockResultList);

        Mockito.when(downloaderService.getArtistsList(TOO_OLD_SEARCH))
                .thenReturn(mockResponse);

        // Test:
        var shouldBeDownloadedArtists = artistService.searchArtists(TOO_OLD_SEARCH);

        assertThat(shouldBeDownloadedArtists.size()).isEqualTo(1);
        assertThat(shouldBeDownloadedArtists.get(0).getArtistId()).isEqualTo(111L);
    }

    @Test
    public void freshSearchShouldUseCache() {
        // Setup:
        final String FRESH_SEARCH = "FreshSearch";

        Mockito.when(lastSearchRepository.findBySearchString(FRESH_SEARCH))
                .thenReturn(new LastSearch(FRESH_SEARCH, Calendar.getInstance().getTime()));


        var mockArtistListInDb = new ArrayList<Artist>();
        mockArtistListInDb.add(Artist.builder()
                .artistName("This Stored Artist")
                .artistId(222L)
                .build());

        Mockito.when(artistRepository.findByArtistNameContainingIgnoreCase(FRESH_SEARCH))
                .thenReturn(mockArtistListInDb);

        // Test:
        var shouldBeDownloadedArtists = artistService.searchArtists(FRESH_SEARCH);

        assertThat(shouldBeDownloadedArtists.size()).isEqualTo(1);
        assertThat(shouldBeDownloadedArtists.get(0).getArtistId()).isEqualTo(222L);
    }

}
