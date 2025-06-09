package ute.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ute.dto.response.ApiResponse;
import ute.dto.response.ReadingSessionResponse;
import ute.dto.response.ReadingStatusResponse;
import ute.dto.response.ReadingSummaryResponse;
import ute.services.ReadingService;

@RestController
@RequestMapping("/reading")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReadingController {
    private final ReadingService readingService;
    
    @PostMapping("/start")
    public ApiResponse<ReadingSessionResponse> startReading(
        @RequestParam Integer userId,
        @RequestParam Integer chapterId
    ) {
        return ApiResponse.<ReadingSessionResponse>builder()
            .data(readingService.startReading(userId, chapterId))
            .build();
    }
    
    @PutMapping("/status")
    public ApiResponse<ReadingStatusResponse> updateReadingStatus(
        @RequestParam Integer userId,
        @RequestParam Integer chapterId
    ) {
        return ApiResponse.<ReadingStatusResponse>builder()
            .data(readingService.updateReadingStatus(userId, chapterId))
            .build();
    }
    
    @PostMapping("/end")
    public ApiResponse<ReadingSummaryResponse> endReading(
        @RequestParam Integer userId,
        @RequestParam Integer chapterId
    ) {
        return ApiResponse.<ReadingSummaryResponse>builder()
            .data(readingService.endReading(userId, chapterId))
            .build();
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
        return ApiResponse.<String>builder()
            .code(500)
            .message(ex.getMessage())
            .data("Error occurred while processing reading session")
            .build();
    }
}
