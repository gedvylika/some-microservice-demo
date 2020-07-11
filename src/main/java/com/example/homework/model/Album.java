package com.example.homework.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Album {

    @Id
    private String albumName;
    private String artistName;
    private String artistLinkUrl;
    private Long artistId;
    private String primaryGenreName;
    private String primaryGenreId;

    private Date cachedDate;
}
