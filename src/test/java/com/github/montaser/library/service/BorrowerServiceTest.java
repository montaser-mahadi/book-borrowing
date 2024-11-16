package com.github.montaser.library.service;

import com.github.montaser.library.exception.RecordNotFoundException;
import com.github.montaser.library.model.Book;
import com.github.montaser.library.model.Borrower;
import com.github.montaser.library.repository.BookRepository;
import com.github.montaser.library.repository.BorrowerRepository;
import com.github.montaser.library.service.impl.BorrowerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.montaser.library.constant.LibraryConstant.BOOK_NOT_BORROWED_BY_THIS_BORROWER;
import static com.github.montaser.library.constant.LibraryConstant.RECORD_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @Test
    void testBorrowBook() {
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> borrowerService.borrowBook(1L, 1L));

        assertNotNull(book.getBorrower());
        assertEquals(borrower, book.getBorrower());
    }

    @Test
    void testBorrowBookAlreadyBorrowed() {
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        book.setBorrower(borrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.borrowBook(1L, 1L));
        assertEquals("Book is already borrowed", exception.getMessage());
    }

    @Test
    void testReturnBook() {
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        book.setBorrower(borrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> borrowerService.returnBook(1L, 1L));

        assertNull(book.getBorrower());
    }

    @Test
    void testReturnBookNotBorrowed() {
        Borrower borrower = new Borrower(1L, "test@example.com", "Test Borrower");
        Borrower anotherBorrower = new Borrower(2L, "another@example.com", "Another Borrower");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        book.setBorrower(anotherBorrower);

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.returnBook(1L, 1L));
        assertEquals(BOOK_NOT_BORROWED_BY_THIS_BORROWER, exception.getMessage());
    }

    @Test
    void testBorrowBook_BorrowerNotFound() {
        Long borrowerId = 1L;
        Long bookId = 1L;

        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> borrowerService.borrowBook(borrowerId, bookId));

        assertEquals(RECORD_DOES_NOT_EXIST, exception.getMessage());
        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        Long borrowerId = 1L;
        Long bookId = 1L;
        Borrower borrower = new Borrower(borrowerId, "borrower@example.com", "Test Borrower");

        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> borrowerService.borrowBook(borrowerId, bookId));

        assertEquals(RECORD_DOES_NOT_EXIST + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }
}
