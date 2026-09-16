package com.example.dataJDBC.controllers;

import com.example.dataJDBC.domain.Author;
import com.example.dataJDBC.domain.dto.AuthorDto;
import com.example.dataJDBC.mappers.Mapper;
import com.example.dataJDBC.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;

    private Mapper<Author, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<Author, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        Author createdAuthor = authorMapper.mapFrom(author);
        Author saveCreatedAuthor = authorService.createAuthor(createdAuthor);
        return new ResponseEntity<>(authorMapper.mapTo(saveCreatedAuthor), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        List<Author> authors = authorService.findAll();
        return authors.stream()
                .map(authorMapper::mapTo)
                .collect(Collectors.toList());

    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id) {
        Optional<Author> foundAuthor = authorService.findOne(id);
        return foundAuthor.map(author -> {
            AuthorDto authorDto = authorMapper.mapTo(author);
            return new ResponseEntity(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));

    }

    @PutMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto
    ){
       if(!authorService.isExists(id)){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }

       authorDto.setId(id);
       Author author = authorMapper.mapFrom(authorDto);

       Author savedAuthor = authorService.createAuthor(author);
       return new ResponseEntity<>(authorMapper.mapTo(savedAuthor),HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto
    ){
        if(authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Author author  = authorMapper.mapFrom(authorDto);
        Author updateAuthor = authorService.partialUpdate(id,author);
        return new ResponseEntity<>(
                authorMapper.mapTo(updateAuthor),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/authors/id")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id){
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

