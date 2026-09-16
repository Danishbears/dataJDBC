package com.example.dataJDBC.controllers;

import com.example.dataJDBC.TestDataUtil;
import com.example.dataJDBC.domain.Author;
import com.example.dataJDBC.domain.dto.AuthorDto;
import com.example.dataJDBC.services.AuthorService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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

public class AuthorControllerIntegrationTest {

    private AuthorService authorService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService){
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception{
        Author testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception{
        Author testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("John Johny")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("77")

        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        ).andExpect(
                                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception{

        Author testAuthorEntity = TestDataUtil.createTestAuthorA();
        authorService.createAuthor(testAuthorEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("John Johny")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value("77")
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus200WhenAuthorExist() throws Exception{
        Author testAuthorA = TestDataUtil.createTestAuthorA();
        authorService.createAuthor(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus404WhenNoAuthorExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/12")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsAuthorWhenAuthorExist() throws Exception{
        Author testAuthorA = TestDataUtil.createTestAuthorA();
        authorService.createAuthor(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("John Johny")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("77")
        );
    }

    @Test
    public void testThatFullUpdateAuthorsReturnsHttpStatus404WhenNoAuthorExist() throws Exception{

        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorsReturnsHttpStatus200WhenAuthorExist() throws Exception{

        Author testAuthor = TestDataUtil.createTestAuthorA();
        Author savedAuthor =  authorService.createAuthor(testAuthor);

        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception{
        Author testAuthor = TestDataUtil.createTestAuthorA();
        Author savedAuthor =  authorService.createAuthor(testAuthor);

        Author testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(savedAuthor.getId());
        String authorDtoUpdateJson = objectMapper.writeValueAsString(testAuthorB);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorB.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorB.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpsStatus200() throws Exception{
        Author testAuthor = TestDataUtil.createTestAuthorA();
        Author savedAuthor =  authorService.createAuthor(testAuthor);

        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoA();
        testAuthorDto.setName("Updated");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );


    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception{
        Author testAuthor = TestDataUtil.createTestAuthorA();
        Author savedAuthor =  authorService.createAuthor(testAuthor);

        AuthorDto testAuthorDto = TestDataUtil.createTestAuthorDtoA();
        testAuthorDto.setName("Updated");
        String authorDtoJson = objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDto.getAge())
        );


    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204NonExistingAuthor() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204ForExistingAuthor() throws Exception{
        Author testAuthor = TestDataUtil.createTestAuthorA();
        Author savedAuthor =  authorService.createAuthor(testAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent());
    }

}
