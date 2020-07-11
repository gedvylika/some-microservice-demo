package com.example.homework.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.homework.repository.Album;
import com.example.homework.repository.Artist;
import com.example.homework.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController {

    @Autowired
    ArtistService artistService;

    @GetMapping("/v1/artist/search")
    @ResponseBody
    public List<ArtistDTO> getArtistsByName(@RequestParam(name = "name", required = true) String name) {
        return transform(artistService.searchArtists(name), artistToDTO);
    }

    @GetMapping("/v1/artist/save/favorite")
    @ResponseBody
    public OperationResult makeArtistFavorite(@RequestParam(name = "artistId", required = true) Long artistId,
                                              @RequestParam(name = "userId", required = true) Long userId) {
        var savedArtist = artistService.makeArtistFavorite(artistId, userId);
        return new OperationResult(savedArtist.getArtistId());
    }

    @GetMapping("/v1/artist/favorite/view/top5albums")
    @ResponseBody
    public List<AlbumDTO> viewTop5Albums(@RequestParam(name = "artistId", required = true) Long artistId,
                                          @RequestParam(name = "userId", required = true) Long userId) {
        var result = artistService.getArtistTop5Albums(artistId, userId);
        return transform(result, albumToDTO);
    }

    public static <K, V, Q extends K> List<V> transform(final List<Q> input, final java.util.function.Function<K, V> tfunc) {
        if (null == input) {
            return null;
        }
        return input.stream().map(tfunc).collect(Collectors.toList());
    }

    Function<Artist, ArtistDTO> artistToDTO =
            new Function<Artist, ArtistDTO>() {
                public ArtistDTO apply(Artist a) {
                    return ArtistDTO.builder()
                            .artistId(a.getArtistId())
                            .artistLinkUrl(a.getArtistLinkUrl())
                            .artistName(a.getArtistName())
                            .build();
                }
            };

    Function<Album, AlbumDTO> albumToDTO =
            new Function<Album, AlbumDTO>() {
                public AlbumDTO apply(Album a) {
                    return AlbumDTO.builder()
                            .albumName(a.getAlbumName())
                            .primaryGenreId(a.getPrimaryGenreId())
                            .primaryGenreName(a.getPrimaryGenreName())
                            .build();
                }
            };
}