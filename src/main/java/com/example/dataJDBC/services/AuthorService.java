package com.example.dataJDBC.services;

import com.example.dataJDBC.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author partialUpdate(Long id, Author author);

    Author createAuthor(Author author);

    List<Author> findAll();

    Optional<Author> findOne(Long id);

    boolean isExists(Long id);

    void delete(Long id);
}
