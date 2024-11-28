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
import ute.dto.response.CommentResponse;
import ute.dto.response.RatingResponse;
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
}
