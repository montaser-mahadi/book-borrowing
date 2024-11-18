// BookService.java
package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.BookDTO;
import java.util.List;

public interface BookService {
    BookDTO registerBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long bookId);
}
