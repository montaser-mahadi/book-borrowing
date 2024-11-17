package com.github.montaser.library.controller;

import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.exception.RecordNotFoundException;
import com.github.montaser.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/register")
    public ResponseEntity<BookDto> registerBook(@RequestBody BookDto bookDto) {
        log.info("Request to register book bookDto: {}", bookDto);
        return new ResponseEntity<>(bookService.registerBook(bookDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() throws RecordNotFoundException {
        log.info("Request to get book all books");
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookDetails(@PathVariable Long bookId) {
        log.info("Request to get book with id: {}", bookId);
        return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
    }
}
