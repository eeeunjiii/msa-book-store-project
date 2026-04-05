package com.example.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Integer publish_year;
    private Integer price;
    private Integer stock;

    public ItemResponse(Long id, String title, String author, String publisher,
                        Integer publish_year, Integer price) {
        this.id=id;
        this.title=title;
        this.author=author;
        this.publisher=publisher;
        this.publish_year=publish_year;
        this.price=price;
    }
}
