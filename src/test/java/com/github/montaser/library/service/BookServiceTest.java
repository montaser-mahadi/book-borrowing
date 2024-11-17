package com.github.montaser.library.service;


import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.mapper.BookMapper;
import com.github.montaser.library.model.Book;
import com.github.montaser.library.repository.BookRepository;
import com.github.montaser.library.repository.BorrowerRepository;
import com.github.montaser.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    private BookDto createBookDTO(String isbn, String title, String author) {
        return new BookDto(null, isbn, title, author, null);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentTitle() {
        BookDto bookDTO = createBookDTO("1234567890", "Different Title", "Test Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);
        
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

}
