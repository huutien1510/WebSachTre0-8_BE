package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ute.dto.request.ApiResponse;
import ute.dto.response.BookDetailResponse;
import ute.dto.response.CommentResponse;
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
    public ApiResponse<List<BookDetailResponse>> getFavoriteBookByAccount(@PathVariable int accountID) {
        ApiResponse<List<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(favoriteBookServices.getFavoriteBookByAccount(accountID));
        return apiResponse;
    }
}
