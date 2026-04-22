package com.example.presentation;

import com.example.domain.Item;
import com.example.application.ItemService;
import com.example.request.NewItemRequest;
import com.example.request.UpdateItemRequest;
import com.example.response.ApiResponse;
import com.example.response.ItemResponse;
import com.example.util.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> items(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<ItemResponse> response=itemService.findAll(page);

        return ResponseEntity.ok(ApiResponse.success(response, "도서 목록 조회 성공"));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItem(@PathVariable("itemId") Long itemId) {
        Item item=itemService.findById(itemId);
        ItemResponse itemResponse=itemMapper.mapToItemResponse(item);

        return ResponseEntity.ok(ApiResponse.success(itemResponse, "도서 조회 성공"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> search(
            @RequestParam(value = "category", defaultValue = "title") String category,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @PageableDefault(sort = "id", size= 10, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ItemResponse> searchList=itemService.search(category, keyword, pageable);

        return ResponseEntity.ok(ApiResponse.success(searchList, "도서 검색 성공"));
    }

    @PostMapping("/manager/new")
    public ResponseEntity<ApiResponse<ItemResponse>> addItem(@RequestBody NewItemRequest newItemRequest) {
        ItemResponse itemResponse = itemService.create(itemMapper.toEntity(newItemRequest));

        return ResponseEntity.ok(ApiResponse.created(itemResponse));
    }

    @PostMapping("/manager/edit/{itemId}")
    public ResponseEntity<ApiResponse<String>> editItem(@Validated @ModelAttribute("item") UpdateItemRequest updateItemRequest,
                           @PathVariable("itemId") Long itemId) {
        itemService.updateItemInfo(itemId, updateItemRequest);
        return ResponseEntity.ok(ApiResponse.success("Success to editing item"));
    }
}
