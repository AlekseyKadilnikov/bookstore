package com.alexeykadilnikov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}
