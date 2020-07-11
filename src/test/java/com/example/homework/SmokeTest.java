package com.example.homework;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.homework.controller.APIController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private APIController controller;

    @Test
    public void contexLoads() {
        assertThat(controller).isNotNull();
    }
}
