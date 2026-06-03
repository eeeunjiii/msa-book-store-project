package com.example.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPageResponse {
    private List<ItemResponse> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
    private boolean isLast;

    public ItemPageResponse(Page<ItemResponse> page) {
        this.content=page.getContent();
        this.pageNumber=page.getNumber();
        this.totalPages=page.getTotalPages();
        this.totalElements=page.getTotalElements();
        this.isLast=page.isLast();
    }
}
