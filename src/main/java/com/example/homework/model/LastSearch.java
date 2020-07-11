package com.example.homework.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LastSearch {

    @Id
    private String searchString;
    private Date timestamp;
}
