package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.request.CommentRequest;
import ute.dto.response.CommentResponse;
import ute.entity.Comment;
import ute.services.CommentServices;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentControllers {
    CommentServices commentServices;

    @GetMapping("chapter/{chapterID}")
    public ApiResponse<List<CommentResponse>> getCommentByChapter(@PathVariable int chapterID) {
        ApiResponse<List<CommentResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(commentServices.getCommentByChapter(chapterID));
        return apiResponse;
    }

    @PostMapping("/post")
    public ApiResponse<Comment> postComment(@RequestBody CommentRequest body){

        ApiResponse<Comment> apiResponse = new ApiResponse<>();
        apiResponse.setData(commentServices.postComment(body));
        return apiResponse;
    }
}
