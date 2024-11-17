package com.github.montaser.library.controller;

import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.dto.BorrowerDto;
import com.github.montaser.library.service.BorrowerService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowerControllerTest {

    @Mock
    private BorrowerService borrowerService;

    @InjectMocks
    private BorrowerController borrowerController;

    private BorrowerDto mockBorrowerDto;

    @BeforeEach
    void setUp() {
        mockBorrowerDto = new BorrowerDto(1L, "test@example.com", "Test Borrower");
    }

    @Test
    void testRegisterBorrower() {
        when(borrowerService.registerBorrower(any(BorrowerDto.class))).thenReturn(mockBorrowerDto);

        ResponseEntity<BorrowerDto> responseEntity = borrowerController.registerBorrower(mockBorrowerDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockBorrowerDto, responseEntity.getBody());
    }

    @Test
    void testBorrowBook() {
        Long borrowerId = 1L;
        Long bookId = 1L;

        doNothing().when(borrowerService).borrowBook(borrowerId, bookId);

        ResponseEntity<String> responseEntity = borrowerController.borrowBook(borrowerId, bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookDtoDemo(), responseEntity.getBody());
    }

    @Test
    void testReturnBook() {
        Long borrowerId = 1L;
        Long bookId = 1L;

        doNothing().when(borrowerService).returnBook(borrowerId, bookId);

        ResponseEntity<String> responseEntity = borrowerController.returnBook(borrowerId, bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookDtoDemo(), responseEntity.getBody());
    }

    @Test
    void testGetAllBorrowers() {
        List<BorrowerDto> mockBorrowers = Collections.singletonList(mockBorrowerDto);
        when(borrowerService.getAllBorrowers()).thenReturn(mockBorrowers);

        ResponseEntity<List<BorrowerDto>> responseEntity = borrowerController.getAllBorrowers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBorrowers, responseEntity.getBody());
    }

    private BookDto bookDtoDemo() {
        return BookDto.builder().build();
    }
}
