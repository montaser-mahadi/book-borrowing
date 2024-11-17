package com.github.montaser.library.mapper;

import com.github.montaser.library.dto.BorrowerDto;
import com.github.montaser.library.model.Borrower;

import java.util.List;

public class BorrowerMapper {

    private BorrowerMapper() {
    }

    public static BorrowerDto toDto(Borrower borrower) {
        return BorrowerDto
                .builder()
                .id(borrower.getId())
                .email(borrower.getEmail())
                .name(borrower.getName())
                .build();
    }

    public static Borrower toEntity(BorrowerDto borrowerDto) {
        return Borrower
                .builder()
                .id(borrowerDto.getId())
                .name(borrowerDto.getName())
                .email(borrowerDto.getEmail())
                .build();
    }

    public static List<BorrowerDto> toDtos(List<Borrower> borrowers) {
        return borrowers.stream().map(BorrowerMapper::toDto).toList();
    }
}
