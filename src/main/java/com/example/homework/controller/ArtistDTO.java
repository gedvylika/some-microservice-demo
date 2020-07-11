package com.example.homework.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtistDTO {
    private Long id;
    private String artistType;
    private String artistName;
    private String artistLinkUrl;
    private Long artistId;
    private String primaryGenreName;
    private Integer primaryGenreId;
}
