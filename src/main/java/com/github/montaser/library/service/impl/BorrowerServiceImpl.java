package com.github.montaser.library.service.impl;

import com.github.montaser.library.constant.LibraryConstant;
import com.github.montaser.library.dto.BorrowerDto;
import com.github.montaser.library.exception.*;
import com.github.montaser.library.mapper.BorrowerMapper;
import com.github.montaser.library.model.Book;
import com.github.montaser.library.model.Borrower;
import com.github.montaser.library.repository.BookRepository;
import com.github.montaser.library.repository.BorrowerRepository;
import com.github.montaser.library.service.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.github.montaser.library.constant.LibraryConstant.*;

@Service
@Slf4j
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;

    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BookRepository bookRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BorrowerDto registerBorrower(BorrowerDto borrowerDto) {
        validatedBorrowerDto(borrowerDto);
        if (borrowerRepository.existsByEmail(borrowerDto.getEmail())) {
            throw new RecordAlreadyExistedException(RECORDS_ALREADY_EXISTS);
        }
        Borrower borrower = BorrowerMapper.toEntity(borrowerDto);
        return BorrowerMapper.toDto(borrowerRepository.save(borrower));
    }

    @Override
    public void returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RecordNotFoundException(RECORD_DOES_NOT_EXIST + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException(BOOK_NOT_BORROWED_BY_THIS_BORROWER + bookId));

        if (book.getBorrower() == null || !book.getBorrower().equals(borrower)) {
            throw new BookNotBorrowedException("Book was not borrowed by this borrower");
        }
        book.setBorrower(null);
        bookRepository.save(book);
    }

    @Override
    public void borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RecordNotFoundException(RECORD_DOES_NOT_EXIST));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException(RECORD_DOES_NOT_EXIST + bookId));

        if (book.getBorrower() != null) {
            throw new BookAlreadyBorrowedException(BOOK_ALREADY_BORROWED);
        }
        book.setBorrower(borrower);
        bookRepository.save(book);
    }

    @Override
    public List<BorrowerDto> getAllBorrowers() {
        List<BorrowerDto> borrowerDtos = BorrowerMapper.toDtos(borrowerRepository.findAll());
        if (borrowerDtos.isEmpty()) {
            throw new RecordNotFoundException(LibraryConstant.RECORDS_DOES_NOT_EXISTS);
        }
        return borrowerDtos;
    }

    @Override
    public BorrowerDto getBorrowerById(Long borrowerId) {
        return findBorrowerById(borrowerId);
    }

    private void validatedBorrowerDto(BorrowerDto borrowerDto) {
        if (Objects.isNull(borrowerDto.getEmail())) {
            throw new DtoValidationException(EMAIL_ADDRESS_IS_REQUIRED);
        }
        if (Objects.isNull(borrowerDto.getName())) {
            throw new DtoValidationException(NAME_IS_REQUIRED);
        }
    }

    public BorrowerDto findBorrowerById(Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RecordNotFoundException(RECORD_DOES_NOT_EXIST));
        return BorrowerMapper.toDto(borrower);
    }
}
