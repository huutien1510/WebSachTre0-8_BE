package ute.controllers;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ute.dto.response.ApiResponse;
import ute.dto.response.AttendanceCheckinResponse;
import ute.entity.Account;
import ute.entity.CheckinHistory;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.AccountRepository;
import ute.repository.CheckHistoryRepository;
import ute.services.AttendanceService;

@RestController
@RequestMapping("/api/test/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceTestController {
    private final AttendanceService attendanceService;
    private final CheckHistoryRepository checkHistoryRepository;
    private final AccountRepository accountRepository;

    // Test 1: Điểm danh bình thường
    @PostMapping("/test-normal/{userId}")
    public ApiResponse<AttendanceCheckinResponse> testNormalCheckin(@PathVariable Integer userId) {
        // Gọi service điểm danh
        AttendanceCheckinResponse response = attendanceService.checkIn(userId);
        
        // Log kết quả
        log.info("Test normal checkin - User: {}, Streak: {}, Points: {}", 
            userId, response.getStreak(), response.getPointsReceived());
        
        return ApiResponse.<AttendanceCheckinResponse>builder()
                .data(response)
                .build();
    }

    // Test 2: Test điểm danh 2 lần trong ngày
    @PostMapping("/test-double/{userId}")
    public ApiResponse<String> testDoubleCheckin(@PathVariable Integer userId) {
        try {
            // Lần 1: Điểm danh bình thường
            attendanceService.checkIn(userId);
            
            // Lần 2: Sẽ throw exception
            attendanceService.checkIn(userId);
            
            return ApiResponse.<String>builder()
                    .message("Test failed: Should not allow double checkin")
                    .build();
        } catch (AppException e) {
            log.info("Test double checkin passed: {}", e.getMessage());
            return ApiResponse.<String>builder()
                    .message("Test passed: Double checkin prevented")
                    .build();
        }
    }

    // Test 3: Test mất streak
    @PostMapping("/test-lost-streak/{userId}")
    public ApiResponse<AttendanceCheckinResponse> testLostStreak(@PathVariable Integer userId) {
        // Tạo lịch sử điểm danh cách đây 2 ngày
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);
        CheckinHistory history = CheckinHistory.builder()
                .account(account)
                .bonusPoint(10)
                .checkinDate(twoDaysAgo)
                .build();
        checkHistoryRepository.save(history);

        // Điểm danh hôm nay
        AttendanceCheckinResponse response = attendanceService.checkIn(userId);
        
        log.info("Test lost streak - User: {}, Streak: {}, Points: {}", 
            userId, response.getStreak(), response.getPointsReceived());
        
        return ApiResponse.<AttendanceCheckinResponse>builder()
                .data(response)
                .build();
    }

    // Test 4: Test giữ streak
    @PostMapping("/test-maintain-streak/{userId}")
    public ApiResponse<AttendanceCheckinResponse> testMaintainStreak(@PathVariable Integer userId) {
        // Tạo lịch sử điểm danh hôm qua
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        LocalDate yesterday = LocalDate.now().minusDays(1);
        CheckinHistory history = CheckinHistory.builder()
                .account(account)
                .bonusPoint(10)
                .checkinDate(yesterday)
                .build();
        checkHistoryRepository.save(history);

        // Điểm danh hôm nay
        AttendanceCheckinResponse response = attendanceService.checkIn(userId);
        
        log.info("Test maintain streak - User: {}, Streak: {}, Points: {}", 
            userId, response.getStreak(), response.getPointsReceived());
        
        return ApiResponse.<AttendanceCheckinResponse>builder()
                .data(response)
                .build();
    }

    // Test 5: Test streak dài (7 ngày)
    @PostMapping("/test-long-streak/{userId}")
    public ApiResponse<AttendanceCheckinResponse> testLongStreak(@PathVariable Integer userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        // Tạo lịch sử điểm danh 7 ngày liên tiếp
        for (int i = 7; i > 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            CheckinHistory history = CheckinHistory.builder()
                    .account(account)
                    .bonusPoint(10)
                    .checkinDate(date)
                    .build();
            checkHistoryRepository.save(history);
        }

        // Điểm danh hôm nay
        AttendanceCheckinResponse response = attendanceService.checkIn(userId);
        
        log.info("Test long streak - User: {}, Streak: {}, Points: {}", 
            userId, response.getStreak(), response.getPointsReceived());
        
        return ApiResponse.<AttendanceCheckinResponse>builder()
                .data(response)
                .build();
    }
}
