package com.github.montaser.library.dto;


import lombok.*;

@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private long id;
    private String title;
    private String author;
    private String isbn;
    private Long borrowerId;
}
