package com.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item", indexes = {
        @Index(name = "idx_item_title", columnList = "title")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String publisher;
    private Integer publish_year;
    private Integer price;
    private int stock;

    @Builder
    public Item(Long id, String title, String author, String publisher,
                Integer publish_year, Integer price, int stock){
        this.id=id;
        this.title=title;
        this.author=author;
        this.publisher=publisher;
        this.publish_year=publish_year;
        this.price=price;
        this.stock=stock;
    }

    public void updateStock(int stock) {
        this.stock=stock;
    }

    public void updateItem(String title, String author, Integer price, int stock) {
        this.title=title;
        this.author=author;
        this.price=price;
        this.stock=stock;
    }
}
