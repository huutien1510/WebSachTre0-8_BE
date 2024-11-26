package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ApiResponse;
import ute.dto.response.BookDetailResponse;
import ute.entity.Chapter;
import ute.services.ChapterServices;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chapters")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChapterControllers {
    ChapterServices chapterServices;

    @GetMapping("/{bookID}/{chapter_number}")
    ApiResponse<Optional<Chapter>> getChapterByBookAndChapterNumber(@PathVariable Integer bookID,
                                                                    @PathVariable Integer chapter_number){
        ApiResponse<Optional<Chapter>> apiResponse = new ApiResponse<>();
        apiResponse.setData(chapterServices.getChapterByBookAndChapterNumber(bookID,chapter_number));
        return apiResponse;
    }
}
