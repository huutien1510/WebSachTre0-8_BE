package ute.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.entity.ReadingProgress;
import ute.services.ReadingProgressService;

import java.util.Map;

@RestController
@RequestMapping("/reading")
@RequiredArgsConstructor
public class ReadingProgressController {
        private final ReadingProgressService readingProgressService;

        @PostMapping("/progress")
        public ApiResponse<Map<String, Object>> updateProgress(
                        @RequestParam Integer userId,
                        @RequestParam Integer secondsRead) {
                ReadingProgress progress = readingProgressService.updateProgress(userId, secondsRead);
                boolean isMissionCompleted = progress.getTotalPoints() >= 20;
                return ApiResponse.<Map<String, Object>>builder()
                                .data(Map.of(
                                                "totalSecondsToday", progress.getTotalSeconds(),
                                                "totalPointsToday", progress.getTotalPoints(),
                                                "isMissionCompleted", isMissionCompleted))
                                .code(200)
                                .build();
        }

        @GetMapping("/progress")
        public ApiResponse<Map<String, Object>> getProgress(@RequestParam Integer userId) {
                ReadingProgress progress = readingProgressService.getProgress(userId);
                boolean isMissionCompleted = progress.getTotalPoints() >= 20;
                return ApiResponse.<Map<String, Object>>builder()
                                .data(Map.of(
                                                "totalSecondsToday", progress.getTotalSeconds(),
                                                "totalPointsToday", progress.getTotalPoints(),
                                                "isMissionCompleted", isMissionCompleted))
                                .code(200)
                                .build();
        }

}