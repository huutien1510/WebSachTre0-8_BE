package ute.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ute.dto.response.AttendanceCheckinResponse;
import ute.dto.response.AttendanceStatusResponse;
import ute.entity.Account;
import ute.entity.CheckinHistory;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.AccountRepository;
import ute.repository.CheckHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService {
    private final AccountRepository accountRepository;
    private final CheckHistoryRepository checkHistoryRepository;

    public AttendanceStatusResponse getStatus(Integer userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        boolean checkedInToday = checkHistoryRepository.existsByAccountIdAndCheckinDate(userId, today);
        int streak = calculateStreak(userId);
        int totalPoints = account.getBonusPoint() != null ? account.getBonusPoint() : 0;
        return new AttendanceStatusResponse(checkedInToday, totalPoints, streak);
    }

    public AttendanceCheckinResponse checkIn(Integer userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        boolean checkedInToday = checkHistoryRepository.existsByAccountIdAndCheckinDate(userId,
                today);
        if (checkedInToday) {
            throw new AppException(ErrorCode.INVALID_REQUEST); // Đã điểm danh hôm nay
        }

        int streak = calculateStreak(userId);
        streak = (streak == 0 || wasCheckedInYesterday(userId)) ? streak + 1 : 1;

        int pointsReceived = 10;
        if (streak > 7) {
            pointsReceived = 20;
        } else if (streak > 3) {
            pointsReceived = 15;
        } else if (streak > 1) {
            pointsReceived = 12;
        }
        int totalPoints = (account.getBonusPoint() != null ? account.getBonusPoint() : 0) + pointsReceived;

        // Cộng điểm cho user
        account.setBonusPoint(totalPoints);
        accountRepository.save(account);

        // Lưu lịch sử điểm danh
        CheckinHistory history = CheckinHistory.builder()
                .account(account)
                .bonusPoint(pointsReceived)
                .checkinDate(today)
                .build();
        checkHistoryRepository.save(history);

        return new AttendanceCheckinResponse(true, pointsReceived, totalPoints, streak);
    }

    private int calculateStreak(Integer userId) {
        List<CheckinHistory> history = checkHistoryRepository.findByAccountIdOrderByCheckinDateDesc(userId);
        if (history.isEmpty())
            return 0;

        int streak = 0;
        LocalDate expected = LocalDate.now();
        for (CheckinHistory h : history) {
            LocalDate date = h.getCheckinDate();
            if (date.equals(expected)) {
                streak++;
                expected = expected.minusDays(1);
            } else if (date.isBefore(expected)) {
                break;
            }
        }
        return streak;
    }

    private boolean wasCheckedInYesterday(Integer userId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return checkHistoryRepository.existsByAccountIdAndCheckinDate(userId, yesterday);
    }
}
