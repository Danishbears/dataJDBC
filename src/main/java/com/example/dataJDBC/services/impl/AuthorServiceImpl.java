package com.example.dataJDBC.services.impl;

import com.example.dataJDBC.domain.Author;
import com.example.dataJDBC.repositories.AuthorRepository;
import com.example.dataJDBC.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author partialUpdate(Long id, Author author) {
        author.setId(id);

        return authorRepository.findById(id).map(
                existingAuthor->{
                    Optional.ofNullable(author.getName()).ifPresent(existingAuthor::setName);
                    Optional.ofNullable(author.getAge()).ifPresent(existingAuthor::setAge);
                    return authorRepository.save(existingAuthor);
                }
        ).orElseThrow(()->new RuntimeException("Author does not exist"));
    }

    @Override
    public Author createAuthor(Author author) {
      return authorRepository.save(author);
    }

    @Override
    public List<Author> findAll() {
      return  StreamSupport.stream(authorRepository
              .findAll().spliterator(),
              false)
              .collect(Collectors.toList());
    }

    @Override
    public Optional<Author> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
