package com.example.dataJDBC.repositories;

import com.example.dataJDBC.TestDataUtil;
import com.example.dataJDBC.domain.Author;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional

public class AuthorRepositoryIntegrationTests {

    private AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest){
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){
        Author author = TestDataUtil.createTestAuthorA();
        Author savedAuthor = underTest.save(author);
        Optional<Author> result =  underTest.findById(savedAuthor.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedAuthor);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);
        Author authorB = TestDataUtil.createTestAuthorB();
        underTest.save(authorB);
        Author authorC = TestDataUtil.createTestAuthorC();
        underTest.save(authorC);

        Iterable<Author> result =  underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(authorA,authorB,authorC);

    }

    @Test
    public void testThatAuthorCanBeUpdated(){
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);
        authorA.setName("UPDATED");
        underTest.save(authorA);
        Optional<Author> result =  underTest.findById(authorA.getId());
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(authorA);

    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);
        underTest.deleteById(authorA.getId());
        Optional<Author> result = underTest.findById(authorA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorWithAgeLessThan(){
        Author testAuthorA = TestDataUtil.createTestAuthorA();
        underTest.save(testAuthorA);
        Author testAuthorB = TestDataUtil.createTestAuthorB();
        underTest.save(testAuthorB);
        Author testAuthorC = TestDataUtil.createTestAuthorC();
        underTest.save(testAuthorC);

        Iterable<Author> result = underTest.ageLessThan(50);
        assertThat(result).containsExactly(testAuthorB);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan(){
        Author testAuthorA = TestDataUtil.createTestAuthorA();
        underTest.save(testAuthorA);
        Author testAuthorB = TestDataUtil.createTestAuthorB();
        underTest.save(testAuthorB);
        Author testAuthorC = TestDataUtil.createTestAuthorC();
        underTest.save(testAuthorC);

        Iterable<Author> result = underTest.findAuthorsWithAgeGreaterThan(50);
        assertThat(result).containsExactly(testAuthorA,testAuthorC);
    }

}
