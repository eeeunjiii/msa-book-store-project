package com.example.repository;

import com.example.domain.Item;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
    List<Item> findByTitle(String title);
    Page<Item> findAll(Pageable pageable);
    Page<Item> findByTitleContaining(String keyword, Pageable pageable);
}
