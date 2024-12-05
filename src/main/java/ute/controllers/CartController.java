package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.response.BookDetailResponse;
import ute.entity.Account;
import ute.services.CartService;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping("/addBookToCart/{accountID}/{bookID}")
    public ApiResponse<Account> addBookToCart(@PathVariable Integer accountID,
                                              @PathVariable Integer bookID)
    {
        ApiResponse<Account> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartService.addBookToCart(accountID,bookID));
        return apiResponse;
    }

    @PostMapping("/removeBookFromCart/{accountID}/{bookID}")
    public ApiResponse<Account> removeBookFromCart(@PathVariable Integer accountID,
                                              @PathVariable Integer bookID)
    {
        ApiResponse<Account> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartService.removeBookFromCart(accountID,bookID));
        return apiResponse;
    }
    @GetMapping("/{accountID}")
    public ApiResponse<List<BookDetailResponse>> getCartByAccount(@PathVariable Integer accountID) {
        ApiResponse<List<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartService.getCartByAccount(accountID));
        return apiResponse;
    }
}
