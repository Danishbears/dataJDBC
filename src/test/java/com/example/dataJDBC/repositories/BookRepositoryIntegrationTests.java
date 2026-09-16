package com.example.dataJDBC.repositories;

import com.example.dataJDBC.TestDataUtil;
import com.example.dataJDBC.domain.Author;
import com.example.dataJDBC.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Sql(scripts = "/schema.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {


    private BookRepository underTest;
    private AuthorRepository authorRepository;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository underTest, AuthorRepository authorRepository) {
        this.underTest = underTest;
        this.authorRepository = authorRepository;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){
        Author author = TestDataUtil.createTestAuthorA();
        Book book = TestDataUtil.createTestBookA(author);
        Book savedBook = underTest.save(book);
        Optional<Book> result = underTest.findById(savedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBook);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        // Step 1 — save author first to get generated id
        Author author = TestDataUtil.createTestAuthorA();
        Author savedAuthor = authorRepository.save(author); // ← capture saved author

        // Step 2 — create and save each book using savedAuthor
        Book bookA = TestDataUtil.createTestBookA(savedAuthor);
        Book savedBookA = underTest.save(bookA); // ← capture returned book

        Book bookB = TestDataUtil.createTestBookB(savedAuthor);
        Book savedBookB = underTest.save(bookB); // ← capture returned book

        Book bookC = TestDataUtil.createTestBookC(savedAuthor);
        Book savedBookC = underTest.save(bookC); // ← capture returned book

        // Step 3 — compare with saved objects, not originals
        Iterable<Book> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(savedBookA, savedBookB, savedBookC); // ← saved versions
    }

    @Test
    public void testThatBookCanBeUpdated(){
        Author author = TestDataUtil.createTestAuthorA();
        Author savedAuthor = authorRepository.save(author);

        Book bookA = TestDataUtil.createTestBookA(savedAuthor);
      //  bookA.setAuthorId(author.getId());
        Book savedBook = underTest.save(bookA);

        savedBook.setTitle("UPDATED");
        Book updatedBook = underTest.save(savedBook);

        Optional<Book> result =  underTest.findById(updatedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedBook);
    }

    @Test
    public void testThatBookCanBeDeleted(){
        Author author = TestDataUtil.createTestAuthorA();
        Author savedAuthor = authorRepository.save(author);

        Book bookA = TestDataUtil.createTestBookA(savedAuthor);
        //bookA.setAuthorId(author.getId());
        Book deletedBook = underTest.save(bookA);

        underTest.delete(deletedBook);

        Optional<Book> result =  underTest.findById(deletedBook.getIsbn());
        assertThat(result).isEmpty();

    }

}
