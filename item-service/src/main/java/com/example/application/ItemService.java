package com.example.application;

import com.example.domain.Item;
import com.example.event.OrderCreatedEvent;
import com.example.repository.ItemRepository;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.request.UpdateItemRequest;
import com.example.response.ItemResponse;
import com.example.util.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public ItemResponse create(Item item) { // ADMIN
        Item savedItem=itemRepository.save(item);
        ItemResponse itemResponse=itemMapper.mapToItemResponse(savedItem);

        return itemResponse;
    }

    public boolean existsItem(Item item) {
        List<Item> findItem=itemRepository.findByTitle(item.getTitle());

        return !findItem.isEmpty();
    }

    @Transactional
    public void updateItemInfo(Long itemId, UpdateItemRequest updateItemRequest) { // ADMIN
        Item item=itemRepository.findById(itemId)
                .orElseThrow(NoSuchElementException::new);

        item.updateItem(updateItemRequest.getTitle(), updateItemRequest.getAuthor(),
                updateItemRequest.getPrice(), updateItemRequest.getStock());
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElse(null);
    }

    public Page<ItemResponse> findAll(int page) {
        Pageable pageable= PageRequest.of(page, 10);

        Page<Item> paging=itemRepository.findAll(pageable);
        return paging.map(item -> new ItemResponse(item.getId(), item.getTitle(), item.getAuthor(),
                item.getPublisher(), item.getPublish_year(), item.getPrice()));
    }

    @KafkaListener(topics = "order-completed-topic", groupId = "item-service-group")
    public synchronized void reduceItemStock(OrderCreatedEvent event) {
        Item item=findById(event.getItemId());

        int newStock;
        if(item.getStock()-event.getQuantity()>=0) {
            newStock=item.getStock()-event.getQuantity();
        } else {
            newStock=item.getStock();
        }
        item.updateStock(newStock);
    }

    @Transactional
    public Page<ItemResponse> search(String category, String keyword, Pageable pageable) {
        Page<Item> items=itemRepository.search(category, keyword, pageable);
        Page<ItemResponse> itemResponses=items.map(itemMapper::mapToItemResponse);

        return itemResponses;
    }
}
