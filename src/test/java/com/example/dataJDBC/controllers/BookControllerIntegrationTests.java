package com.example.dataJDBC.controllers;


import com.example.dataJDBC.TestDataUtil;
import com.example.dataJDBC.domain.Book;
import com.example.dataJDBC.domain.dto.BookDto;
import com.example.dataJDBC.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private BookService bookService;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsHttpsStatus201Created() throws Exception{

        Book testBookA = TestDataUtil.createTestBookA(null);
        Book savedBook = bookService.createUpdateBook(testBookA.getIsbn(),testBookA);
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        testBookA.setIsbn(savedBook.getIsbn());
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    public void testThatCreateBookReturnsUpdatedBook() throws Exception{

        Book testBookA = TestDataUtil.createTestBookA(null);
        Book savedBook = bookService.createUpdateBook(testBookA.getIsbn(),testBookA);
        //BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        testBookA.setIsbn(savedBook.getIsbn());
        testBookA.setTitle("UPDATED");
        String createBookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED")
        );

    }

    @Test
    public void testThatListBookReturnsHttpsStatus200Ok() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    public void testThatListBooksReturnsBook() throws Exception{

        Book testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(),testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value("978-1-2345-6767-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value("Chinazes")
        );
    }

    @Test
    public void testThatGetBookReturnsHttpsStatus200OkWhenBookExists() throws Exception{

        Book testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(),testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    public void testThatGetBookReturnsHttpsStatus200OkWhenBookDoesNotExists() throws Exception{

        Book testBookEntity = TestDataUtil.createTestBookA(null);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }

    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus200Ok() throws Exception{
        Book testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(),testBookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        testBookEntity.setTitle("Updated");
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception{
        Book testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(),testBookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        testBookEntity.setTitle("Updated");
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("Updated")
        );
    }

    @Test
    public void testThatDeleteNonExistingBookReturnsHttp204NoContent() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/1331315jansnqx")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteExistingBookReturnsHttp204NoContent() throws Exception{
        Book testBookEntity = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(),testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/"+testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
