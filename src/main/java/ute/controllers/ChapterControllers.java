package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.response.ChapterResponse;
import ute.services.ChapterServices;

import java.util.List;

@RestController
@RequestMapping("/chapters")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterControllers {
    ChapterServices chapterServices;

    @GetMapping("/{bookID}")
    ApiResponse<List<ChapterResponse>> getChapterByBook(@PathVariable Integer bookID){
        ApiResponse<List<ChapterResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(chapterServices.getChapterByBook(bookID));
        return apiResponse;
    }

    @GetMapping("/totalView")
    ApiResponse<Long> getTotalView(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(chapterServices.getTotalView());
        return apiResponse;
    }

    @PatchMapping("/upView/{chapterID}")
    ApiResponse<Integer> upViewChapter(@PathVariable Integer chapterID){
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setData(chapterServices.upViewChapter(chapterID));
        return apiResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(500);
        apiResponse.setMessage(ex.toString());
        apiResponse.setData("Fault data");
        return apiResponse;
    }
}
