package com.example.dataJDBC.services;

import com.example.dataJDBC.domain.Book;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createUpdateBook(String isbn, Book book);

    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

    Optional<Book> findOne(String isbn);

    Boolean isExists(String isbn);

    Book partialUpdate(String isbn, Book book);

    void delete(String isbn);
}
