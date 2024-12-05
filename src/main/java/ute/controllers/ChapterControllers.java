package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ChapterAddRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.ChapterResponse;
import ute.entity.Chapter;
import ute.services.ChapterServices;

import java.util.List;
import java.util.Map;

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
        apiResponse.setCode(200);
        apiResponse.setData(chapterServices.getChapterByBook(bookID));
        return apiResponse;
    }

    @GetMapping("/chapter/{chapterID}")
    ApiResponse<ChapterResponse> getChapterByID(@PathVariable Integer chapterID){
        ApiResponse<ChapterResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(chapterServices.getChapterByID(chapterID));
        return apiResponse;
    }

    @GetMapping("/totalView")
    ApiResponse<Long> getTotalView(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(chapterServices.getTotalView());
        return apiResponse;
    }

    @PostMapping("/addChapter")
    ApiResponse<Chapter> getTotalView(@RequestBody ChapterAddRequest body){
        ApiResponse<Chapter> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(chapterServices.addChapter(body));
        return apiResponse;
    }

    @PatchMapping("/upView/{chapterID}")
    ApiResponse<Integer> upViewChapter(@PathVariable Integer chapterID){
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(chapterServices.upViewChapter(chapterID));
        return apiResponse;
    }

    @PatchMapping("/updateChapter/{chapterID}")
    ApiResponse<Chapter> updateChapterTitle(@PathVariable Integer chapterID,
                                       @RequestBody Map<String,String> body){
        ApiResponse<Chapter> apiResponse = new ApiResponse<>();
        String newTitle = body.get("title");
        apiResponse.setCode(200);
        apiResponse.setData(chapterServices.updateChapter(chapterID,newTitle));
        return apiResponse;
    }

    @DeleteMapping("/deleteChapter/{chapterID}")
    ApiResponse<String> deleteChapter(@PathVariable Integer chapterID){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        chapterServices.deleteChapter(chapterID);
        apiResponse.setCode(200);
        apiResponse.setData("Xóa thành công");
        return apiResponse;
    }

}
