package com.example.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewItemRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String publisher;

    @NotNull
    private Integer publish_year;

    @NotNull
    private Integer price;

    @NotNull
    private Integer stock;
}
