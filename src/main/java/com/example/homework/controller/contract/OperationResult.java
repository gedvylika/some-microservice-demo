package com.example.homework.controller.contract;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationResult {
    private Long savedArtistId;

    public OperationResult(Long id) {
        savedArtistId = id;
    }
}
