package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.response.BookDetailResponse;
import ute.entity.Article;
import ute.services.ArticleServices;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleControllers {
    ArticleServices articleServices;

    @GetMapping("/getAll")
    ApiResponse<Page<Article>> getAllArticles(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<Article>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(articleServices.getAllArticles(page,size));
        return apiResponse;
    }
    @PostMapping
    public ApiResponse<Void> addArticle(@RequestBody Article article){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        articleServices.addArticle(article);
        apiResponse.setCode(200);
        apiResponse.setMessage("Add article successfully");
        return apiResponse;
    }
}
