package com.example.dataJDBC;

import com.example.dataJDBC.domain.Author;
import com.example.dataJDBC.domain.Book;
import com.example.dataJDBC.domain.dto.AuthorDto;
import com.example.dataJDBC.domain.dto.BookDto;

//!!!!!!!! -> add ID again, but check if it's possible to operate with manual and automatic ID generators


public final class TestDataUtil {
    private TestDataUtil(){}

    public static Author createTestAuthorA() {
        return Author.builder()
                .name("John Johny")
                .age(77)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .name("John Johny")
                .age(77)
                .build();
    }

    public static Author createTestAuthorB() {
        return Author.builder()
                .name("Jonah Jovovic")
                .age(23)
                .build();
    }

    public static Author createTestAuthorC() {
        return Author.builder()
                .name("Juan Suarez")
                .age(67)
                .build();
    }

    public static Book createTestBookA(final Author author){
        return Book.builder()
                .isbn("978-1-2345-6767-0")
                .title("Chinazes")
                .author(author)
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto author){
        return BookDto.builder()
                .isbn("978-1-2345-6767-0")
                .title("Chinazes")
                .author(author)
                .build();
    }

    public static Book createTestBookB(final Author author){
        return Book.builder()
                .isbn("978-1-2345-6767-1")
                .title("Blood Meridian")
                .author(author)
                .build();
    }

    public static Book createTestBookC(final Author author){
        return Book.builder()
                .isbn("978-1-2345-6767-2")
                .title("Silver spine")
                .author(author)
                .build();
    }
}
