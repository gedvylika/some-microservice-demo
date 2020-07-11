package com.example.homework.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlbumDTO {
    private String albumName;
    private String primaryGenreName;
    private String primaryGenreId;
}
