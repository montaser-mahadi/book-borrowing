package com.github.montaser.library.dto;

import lombok.*;


@EqualsAndHashCode
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerDto {

    private Long id;

    private String name;
    private String email;
}

