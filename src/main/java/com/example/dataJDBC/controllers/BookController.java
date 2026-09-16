package com.example.dataJDBC.controllers;

import com.example.dataJDBC.domain.Book;
import com.example.dataJDBC.domain.dto.BookDto;
import com.example.dataJDBC.mappers.Mapper;
import com.example.dataJDBC.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<Book, BookDto> bookMapper;

    private BookService bookService;

    public BookController(Mapper<Book, BookDto> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn,
                                                    @RequestBody BookDto bookDto
    ){
        Book book = bookMapper.mapFrom(bookDto);
        Boolean bookExists = bookService.isExists(isbn);
        Book savedBook = bookService.createUpdateBook(isbn,book);
        BookDto savedBookDto = bookMapper.mapTo(savedBook);

        if(bookExists){
            return new ResponseEntity<>(savedBookDto,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto
    ){

        boolean bookExists = bookService.isExists(isbn);
        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Book book = bookMapper.mapFrom(bookDto);
        Book updatedBook = bookService.partialUpdate(isbn,book);
        return new ResponseEntity<>(bookMapper.mapTo(updatedBook),HttpStatus.OK);
    }



    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(Pageable pageable){
       Page<Book> books =  bookService.findAll(pageable);
       return books.map(bookMapper::mapTo);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<Book> foundBook =  bookService.findOne(isbn);
        return foundBook.map(bookEntity ->{
                BookDto bookDto = bookMapper.mapTo(bookEntity);
                return new ResponseEntity<>(bookDto,HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
