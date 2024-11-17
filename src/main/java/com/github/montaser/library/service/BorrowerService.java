package com.github.montaser.library.service;

import com.github.montaser.library.dto.BorrowerDto;

import java.util.List;

public interface BorrowerService {

    BorrowerDto registerBorrower(BorrowerDto borrower);

    void returnBook(Long borrowerId, Long bookId);

    void borrowBook(Long borrowerId, Long bookId);

    List<BorrowerDto> getAllBorrowers();

    BorrowerDto getBorrowerById(Long borrowerId);
}
