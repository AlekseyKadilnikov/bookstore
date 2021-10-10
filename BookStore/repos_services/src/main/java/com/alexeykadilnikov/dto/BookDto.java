package com.alexeykadilnikov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto extends BaseEntityDto {
    private String name;
    private Set<AuthorDto> authors;
    private String publisher;
    private int publicationYear;
    private int price;
    private String description;
    private int count;
    private LocalDate dateOfReceipt;

    public BookDto(String name, int publicationYear, int price, int count, String description) {
        this.name = name;
        this.publicationYear = publicationYear;
        this.price = price;
        this.count = count;
        this.description = description;
    }
}
