package com.alexeykadilnikov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto extends BaseEntityDto{
    private String firstName;
    private String lastName;
    private String middleName;
}
