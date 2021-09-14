package com.alexeykadilnikov.dto;

import com.alexeykadilnikov.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto extends BaseEntityDto {
    private String name;
    private int count;
    private RequestStatus status;
}
