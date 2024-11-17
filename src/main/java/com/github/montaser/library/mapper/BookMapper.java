package com.github.montaser.library.mapper;

import com.github.montaser.library.dto.BookDto;
import com.github.montaser.library.dto.BorrowerDto;
import com.github.montaser.library.model.Book;

import java.util.List;

public class BookMapper {

    private BookMapper() {
    }

    public static BookDto toDto(Book book) {
        return BookDto
                .builder()
                .id(book.getId())
                .author(book.getAuthor())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .borrowerId(book.getBorrower().getId())
                .build();
    }

    public static Book toEntity(BookDto bookDto) {
        return Book
                .builder()
                .id(bookDto.getId())
                .author(bookDto.getAuthor())
                .title(bookDto.getTitle())
                .isbn(bookDto.getIsbn())
                .borrower(BorrowerMapper.toEntity(BorrowerDto.builder().id(bookDto.getBorrowerId()).build()))
                .build();
    }

    public static List<BookDto> toDtos(List<Book> books) {
        return books.stream().map(BookMapper::toDto).toList();
    }
}