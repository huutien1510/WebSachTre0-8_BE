package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.response.ChapterContentResponse;
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
    public List<ChapterContentResponse> getContentByChapter(@PathVariable int chapterID) {
        return chapterContentService.getContentByChapter(chapterID);
    }

}
