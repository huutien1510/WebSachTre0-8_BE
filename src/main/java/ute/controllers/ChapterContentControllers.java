package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ChapterContentRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.ChapterContentResponse;
import ute.entity.ChapterContent;
import ute.services.ChapterContentService;

import java.util.List;

@RestController
@RequestMapping("/chaptercontents")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterContentControllers {
    ChapterContentService chapterContentService;

    @GetMapping("/{chapterID}")
    public ApiResponse<List<ChapterContentResponse>> getContentByChapter(@PathVariable Integer chapterID) {
        ApiResponse<List<ChapterContentResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(chapterContentService.getContentByChapter(chapterID));
        return apiResponse;
    }

    @PostMapping("/addContent")
    public ApiResponse<ChapterContent> addContent(@RequestBody ChapterContentRequest body){
        ApiResponse<ChapterContent> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(chapterContentService.addContent(body));
        return apiResponse;
    }

    @DeleteMapping("deleteContent/{contentID}")
    public ApiResponse<String> deleteContentByID(@PathVariable Integer contentID) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        chapterContentService.deleteContentByID(contentID);
        apiResponse.setCode(200);
        apiResponse.setData("Xóa thành công");
        return apiResponse;
    }

}
