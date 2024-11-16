package com.github.montaser.library.service.impl;

import com.github.montaser.library.constant.LibraryConstant;
import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.exception.DtoValidationException;
import com.github.montaser.library.exception.RecordNotFoundException;
import com.github.montaser.library.mapper.BookMapper;
import com.github.montaser.library.model.Book;
import com.github.montaser.library.model.Borrower;
import com.github.montaser.library.repository.BookRepository;
import com.github.montaser.library.repository.BorrowerRepository;
import com.github.montaser.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.github.montaser.library.constant.LibraryConstant.*;
import static com.github.montaser.library.mapper.BookMapper.toDtos;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BookServiceImpl(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public BookDto registerBook(BookDto bookDto) {
        log.info("bookDto {}", bookDto);
        validatedBookDto(bookDto);
        Book book = BookMapper.toEntity(bookDto);
        if (!Objects.isNull(book.getBorrower()) && bookDto.getBorrowerId() != 0) {
            Borrower borrower = borrowerRepository.findById(bookDto.getBorrowerId())
                    .orElseThrow(() -> new RecordNotFoundException(RECORD_DOES_NOT_EXIST));
            book.setBorrower(borrower);
        } else {
            book.setBorrower(null);
        }
        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getAllBooks() throws RecordNotFoundException {
        List<BookDto> bookDtos = toDtos(bookRepository.findAll());
        if (bookDtos.isEmpty()) {
            throw new RecordNotFoundException(LibraryConstant.RECORDS_DOES_NOT_EXISTS);
        }
        return bookDtos;
    }

    @Override
    public BookDto getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException(RECORDS_DOES_NOT_EXISTS + bookId));
        return BookMapper.toDto(book);
    }

    private void validatedBookDto(BookDto bookDto) {
        if (Objects.isNull(bookDto.getIsbn())) {
            throw new DtoValidationException(ISBN_IS_REQUIRED);
        }
        if (Objects.isNull(bookDto.getAuthor())) {
            throw new DtoValidationException(AUTHOR_IS_REQUIRED);
        }
        if (Objects.isNull(bookDto.getTitle())) {
            throw new DtoValidationException(TITLE_IS_REQUIRED);
        }
        List<Book> existingBooks = bookRepository.findByIsbn(bookDto.getIsbn());
        for (Book existingBook : existingBooks) {
            if (!existingBook.getTitle().equals(bookDto.getTitle()) ||
                    !existingBook.getAuthor().equals(bookDto.getAuthor())) {
                throw new IllegalArgumentException(BOOK_WITH_SAME_TITLE_AND_AUTHOR);
            }
        }
    }
}

