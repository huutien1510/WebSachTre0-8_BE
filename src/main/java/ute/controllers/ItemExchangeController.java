package ute.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import ute.dto.response.AccountItemResponse;
import ute.dto.response.ApiResponse;
import ute.dto.response.ItemExchangeHistoryResponse;
import ute.services.ItemExchangeService;

@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ItemExchangeController {
    private final ItemExchangeService itemExchangeService;

    @PostMapping
    public ApiResponse<String> exchangeItem(@RequestBody Map<String, Integer> request) {
        Integer accountId = request.get("accountId");
        Integer itemId = request.get("itemId");
        String result = itemExchangeService.exchangeItem(accountId, itemId);
        return ApiResponse.<String>builder()
                .code(200)
                .message(result)
                .build();
    }

    @GetMapping("/history/{accountId}")
    public ApiResponse<List<ItemExchangeHistoryResponse>> getExchangeHistory(@PathVariable Integer accountId) {
        List<ItemExchangeHistoryResponse> history = itemExchangeService.getExchangeHistory(accountId);
        return ApiResponse.<List<ItemExchangeHistoryResponse>>builder()
                .code(200)
                .data(history)
                .build();
    }

    @GetMapping("/inventory/{accountId}")
    public ApiResponse<List<AccountItemResponse>> getAccountItemInventory(@PathVariable Integer accountId) {
        List<AccountItemResponse> inventory = itemExchangeService.getAccountItemInventory(accountId);
        return ApiResponse.<List<AccountItemResponse>>builder()
                .code(200)
                .data(inventory)
                .build();
    }
}

