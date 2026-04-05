package com.example.repository;


import com.example.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomRepository {

    Page<Item> search(String category, String keyword, Pageable pageable);
}
