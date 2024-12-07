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

    @GetMapping("/{articleID}")
    ApiResponse<Article> getArticlesByID(@PathVariable Integer articleID){
        ApiResponse<Article> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(articleServices.getArticlesByID(articleID));
        return apiResponse;
    }

    @PostMapping("/addArticle")
    public ApiResponse<Article> addArticle(@RequestBody Article article){
        ApiResponse<Article> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Add article successfully");
        apiResponse.setData(articleServices.addArticle(article));
        return apiResponse;
    }

    @PatchMapping("/editArticle/{articleID}")
    public ApiResponse<Article> editArticle(@PathVariable Integer articleID,
                                            @RequestBody Article body){
        ApiResponse<Article> apiResponse = new ApiResponse<>();
        System.out.println(body.getDate());
        apiResponse.setCode(200);
        apiResponse.setMessage("Edit article successfully");
        apiResponse.setData(articleServices.editArticle(articleID,body));
        return apiResponse;
    }

    @DeleteMapping("/deleteArticle/{articleID}")
    public ApiResponse<Void> deleteArticle(@PathVariable Integer articleID){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        articleServices.deleteArticle(articleID);
        apiResponse.setCode(200);
        apiResponse.setMessage("Delete article successfully");
        return apiResponse;
    }

}
