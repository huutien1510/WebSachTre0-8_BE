package ute.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.services.ItemExchangeService;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ItemExchangeController {
    private final ItemExchangeService itemExchangeService;

    @PostMapping
    public ApiResponse<String> exchangeItem(@RequestParam Integer accountId, @RequestParam Integer itemId) {
        String result = itemExchangeService.exchangeItem(accountId, itemId);
        return ApiResponse.<String>builder()
                .code(200)
                .message(result)
                .build();
    }
}