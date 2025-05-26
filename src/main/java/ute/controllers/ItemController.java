package ute.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import ute.dto.request.ItemRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.ItemResponse;
import ute.services.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // Lấy tất cả item (hiển thị lên trang đổi thưởng)
//    @GetMapping
//    public ApiResponse<List<ItemResponse>> getAllItems() {
//        return itemService.getAllItems();
//    }
    @GetMapping("/getAll")
    public ApiResponse<Page<ItemResponse>> getAllItems(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getAllItems(page, size);
    }

    // Lấy tất cả item cho user (hiển thị lên trang đổi thưởng)
    @GetMapping("/getAllForUser")
    public ApiResponse<Page<ItemResponse>> getAllItemsForUser(@RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getAllItemsForUser(page, size);
    }
    // Thêm mới item
    @PostMapping
    public ApiResponse<ItemResponse> addItem(@RequestBody ItemRequest request) {
        return itemService.addItem(request);
    }

    // Sửa item
    @PutMapping("/{id}")
    public ApiResponse<ItemResponse> updateItem(@PathVariable Integer id, @RequestBody ItemRequest request) {
        return itemService.updateItem(id, request);
    }

    // Xóa item
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteItem(@PathVariable Integer id) {
        return itemService.deleteItem(id);
    }
}
