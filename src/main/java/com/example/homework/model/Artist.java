package com.example.homework.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Artist {

    @Id
    private Long artistId;
    private String artistName;
    private String artistLinkUrl;
    private String primaryGenreName;
    private String primaryGenreId;
}
