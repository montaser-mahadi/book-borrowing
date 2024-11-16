package com.github.montaser.library.controller;

import com.github.montaser.library.dto.BorrowerDto;
import com.github.montaser.library.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrowers")
@Slf4j
public class BorrowerController {

    private final BorrowerService borrowerService;


    @PostMapping
    public ResponseEntity<BorrowerDto> registerBorrower(@RequestBody BorrowerDto borrowerDTO) {
        log.info("Request to create borrower: {}", borrowerDTO);
        BorrowerDto savedBorrower = borrowerService.registerBorrower(borrowerDTO);
        return new ResponseEntity<>(savedBorrower, HttpStatus.CREATED);
    }

    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        log.info("Request to borrow book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        log.info("Request to return book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping
    public ResponseEntity<List<BorrowerDto>> getAllBorrowers() {
        log.info("Request to get all borrowers");
        return new ResponseEntity<>(borrowerService.getAllBorrowers(), HttpStatus.OK);
    }

    @GetMapping("/{borrowerId}")
    public ResponseEntity<BorrowerDto> getBorrowerDetails(@PathVariable Long borrowerId) {
        log.info("Request to get borrower with id: {}", borrowerId);
        return new ResponseEntity<>(borrowerService.getBorrowerById(borrowerId), HttpStatus.OK);
    }
}

