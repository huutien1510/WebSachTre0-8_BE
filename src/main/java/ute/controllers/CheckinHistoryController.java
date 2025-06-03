package ute.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ute.dto.response.AttendanceCheckinResponse;
import ute.dto.response.AttendanceStatusResponse;
import ute.services.AttendanceService;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class CheckinHistoryController {
    private final AttendanceService attendanceService;

    @GetMapping("/status")
    public AttendanceStatusResponse getStatus(@RequestParam Integer userId) {
        return attendanceService.getStatus(userId);
    }

    @PostMapping("/check-in")
    public AttendanceCheckinResponse checkIn(@RequestBody UserIdRequest request) {
        return attendanceService.checkIn(request.getUserId());
    }

    @lombok.Data
    public static class UserIdRequest {
        private Integer userId;
    }
}