package com.github.montaser.library.service;

import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.exception.RecordNotFoundException;

import java.util.List;

public interface BookService {

    BookDto registerBook(BookDto bookDto);

    List<BookDto> getAllBooks() throws RecordNotFoundException;

    BookDto getBookById(Long bookId);
}
