package com.example.homework.repository;
import com.example.homework.model.LastSearch;
import org.springframework.data.repository.CrudRepository;

public interface LastSearchRepository extends CrudRepository<LastSearch, Long> {
    LastSearch findBySearchString(String name);
}