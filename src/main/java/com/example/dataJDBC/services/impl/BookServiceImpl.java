package com.example.dataJDBC.services.impl;

import com.example.dataJDBC.domain.Book;
import com.example.dataJDBC.repositories.BookRepository;
import com.example.dataJDBC.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createUpdateBook(String isbn, Book book) {
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
       return StreamSupport.stream(
               bookRepository.findAll().spliterator(),
               false
       ).collect(Collectors.toList());
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }


    @Override
    public Optional<Book> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public Boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public Book partialUpdate(String isbn, Book book) {
        book.setIsbn(isbn);

        return bookRepository.findById(isbn).map(existingBook->{
            Optional.ofNullable(book.getTitle()).ifPresent(existingBook::setTitle);
            return bookRepository.save(existingBook);
        }).orElseThrow(()->new RuntimeException("Book does not exist"));
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
