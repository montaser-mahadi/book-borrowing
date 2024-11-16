package com.github.montaser.library.controller;


import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto mockBookDto;

    @BeforeEach
    void setUp() {
        mockBookDto = new BookDto(1L, "1234567890", "Test Book", "Test Author", null);
    }

    @Test
    void testRegisterBook() {
        // Mocking behavior of BookService
        when(bookService.registerBook(any(BookDto.class))).thenReturn(mockBookDto);

        // Call the controller method
        ResponseEntity<BookDto> responseEntity = bookController.registerBook(mockBookDto);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockBookDto, responseEntity.getBody());
    }

    @Test
    void testGetAllBooks() {
        List<BookDto> mockBooks = Collections.singletonList(mockBookDto);
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        ResponseEntity<List<BookDto>> responseEntity = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks, responseEntity.getBody());
    }
}
