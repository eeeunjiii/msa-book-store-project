package com.example.presentation;

import com.example.application.ItemService;
import com.example.response.ItemPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public ResponseEntity<ItemPageResponse> index(@RequestParam(value = "page", defaultValue = "0") int page) {
        ItemPageResponse response=itemService.findAll(page);

        return ResponseEntity.ok(response);
    }
}
