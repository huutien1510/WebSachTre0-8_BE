package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.response.BookDetailResponse;
import ute.entity.Account;
import ute.services.FavoriteBookServices;

import java.util.List;

@RestController
@RequestMapping("/favbooks")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteBookControllers {
    FavoriteBookServices favoriteBookServices;

    @GetMapping("account/{accountID}")
    public ApiResponse<List<BookDetailResponse>> getFavoriteBookByAccount(@PathVariable Integer accountID) {
        ApiResponse<List<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(favoriteBookServices.getFavoriteBookByAccount(accountID));
        return apiResponse;
    }

    @GetMapping("checkIsFavorites/{accountID}/{bookID}")
    public ApiResponse<Boolean> checkIsFavoritesByAccount(@PathVariable Integer accountID,
                                                          @PathVariable Integer bookID) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setData(favoriteBookServices.checkIsFavoritesByAccount(accountID,bookID));
        return apiResponse;
    }

    @PostMapping("/addFavorite/{accountID}/{bookID}")
    public ApiResponse<Account> addFavorite(@PathVariable Integer accountID,
                                            @PathVariable Integer bookID)
    {
        ApiResponse<Account> apiResponse = new ApiResponse<>();
        apiResponse.setData(favoriteBookServices.addFavorite(accountID,bookID));
        return apiResponse;
    }

    @DeleteMapping("/removeFavorite/{accountID}/{bookID}")
    public ApiResponse<Account> removeFavorite(@PathVariable Integer accountID,
                                            @PathVariable Integer bookID)
    {
        ApiResponse<Account> apiResponse = new ApiResponse<>();
        apiResponse.setData(favoriteBookServices.removeFavorite(accountID,bookID));
        return apiResponse;
    }

}
