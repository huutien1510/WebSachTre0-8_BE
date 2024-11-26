package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ApiResponse;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;
import ute.services.BookServices;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookControllers {
    BookServices bookServices;

    @GetMapping
    ApiResponse<Page<BookDetailResponse>> getAllBooks(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookServices.getAllBook(page,size));
        return apiResponse;
    }

    @GetMapping("/search")
    ApiResponse<Page<BookDetailResponse>> getBookBySearch(@RequestParam(required = false) String keyword,
                                                          @RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookServices.getBookByKeyWord(keyword,page,size));
        return apiResponse;
    }

    @GetMapping("/freebooks")
    ApiResponse<Page<BookDetailResponse>> getFreeBook(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookServices.getFreeBook(page,size));
        return apiResponse;
    }

    @GetMapping("/feebooks")
    ApiResponse<Page<BookDetailResponse>> getFeeBook(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookServices.getFeeBook(page,size));
        return apiResponse;
    }

    @GetMapping("/genre/{genreID}")
    ApiResponse<Page<BookDetailResponse>> getBookByGenre(@PathVariable Integer genreID,
                                                         @RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<BookDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookServices.getBookByGenre(genreID,page,size));
        return apiResponse;
    }
}
