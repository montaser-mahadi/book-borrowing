package com.github.montaser.library.service;


import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.exception.RecordNotFoundException;
import com.github.montaser.library.mapper.BookMapper;
import com.github.montaser.library.model.Book;
import com.github.montaser.library.model.Borrower;
import com.github.montaser.library.repository.BookRepository;
import com.github.montaser.library.repository.BorrowerRepository;
import com.github.montaser.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto createBookDto(String isbn, String title, String author) {
        return new BookDto(1, isbn, title, author, null);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterBook() {
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Test Author");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);

        when(BookMapper.toEntity(bookDTO)).thenReturn(book);
        when(BookMapper.toDto(book)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto savedBook = bookService.registerBook(bookDTO);

        assertEquals(bookDTO.getIsbn(), savedBook.getIsbn());
        assertEquals(bookDTO.getTitle(), savedBook.getTitle());
        assertEquals(bookDTO.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testRegisterBookWithExistingISBNAndSameTitleAuthor() {
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Test Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);

        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);
        when(BookMapper.toEntity(bookDTO)).thenReturn(existingBook);

        assertDoesNotThrow(() -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentTitle() {
        BookDto bookDTO = createBookDto("1234567890", "Different Title", "Test Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);

        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentAuthor() {
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Different Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);

        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book(1L, "1234567890", "Book 1", "Author 1", null);
        Book book2 = new Book(2L, "0987654321", "Book 2", "Author 2", null);
        List<Book> mockBooks = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(mockBooks);
        when(BookMapper.toDto(book1)).thenReturn(new BookDto(book1.getId(), book1.getIsbn(), book1.getTitle(), book1.getAuthor(), null));
        when(BookMapper.toDto(book2)).thenReturn(new BookDto(book2.getId(), book2.getIsbn(), book2.getTitle(), book2.getAuthor(), null));

        List<BookDto> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        assertEquals(book1.getIsbn(), books.get(0).getIsbn());
        assertEquals(book2.getIsbn(), books.get(1).getIsbn());
    }


    @Test
    void testRegisterBookWithValidBorrowerId() {
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        bookDto.setBorrowerId(1L);
        Borrower borrower = new Borrower(1L, "doe@john.com", "John Doe");

        when(BookMapper.toEntity(bookDto)).thenReturn(new Book());
        when(BookMapper.toDto(new Book())).thenReturn(bookDto);
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        BookDto savedBook = bookService.registerBook(bookDto);

        assertNotNull(savedBook);
        assertEquals(bookDto.getBorrowerId(), savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithInvalidBorrowerId() {
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Test Author");
        bookDTO.setBorrowerId(999L);

        when(BookMapper.toEntity(bookDTO)).thenReturn(new Book());
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithNullBorrowerId() {
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        bookDto.setBorrowerId(null);

        when(BookMapper.toEntity(bookDto)).thenReturn(new Book());
        when(BookMapper.toDto(new Book())).thenReturn(bookDto);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        BookDto savedBook = bookService.registerBook(bookDto);

        assertNotNull(savedBook);
        assertNull(savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithZeroBorrowerId() {
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        bookDto.setBorrowerId(0L);

        when(BookMapper.toEntity(bookDto)).thenReturn(new Book());
        when(BookMapper.toDto(new Book())).thenReturn(bookDto);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        BookDto savedBook = bookService.registerBook(bookDto);

        assertNotNull(savedBook);
        assertEquals(0L, savedBook.getBorrowerId());
    }

}
