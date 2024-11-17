package com.github.montaser.library.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookDto testBookDto;

    @BeforeEach
    void setUp() {
        testBookDto = new BookDto();
        testBookDto.setId(1L);
        testBookDto.setTitle("Java Advance");
        testBookDto.setAuthor("John Doe");
        testBookDto.setIsbn("1234567890");
        testBookDto.setBorrowerId(null);
    }

    @Test
    void testRegisterBook() throws Exception {
        Mockito.when(bookService.registerBook(Mockito.any(BookDto.class))).thenReturn(testBookDto);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookDto)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testBookDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(testBookDto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDto.getIsbn()));
    }
}
