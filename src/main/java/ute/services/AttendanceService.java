package ute.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        // Kiểm tra số lần khôi phục còn lại trong tháng
        int remainingRecoveries = getRemainingRecoveries(userId);

        // Kiểm tra xem có thể khôi phục không
        boolean canRecover = canRecoverStreak(userId);

        // Kiểm tra xem có phải lần đầu điểm danh không
        boolean isFirstCheckIn = checkHistoryRepository.findByAccountIdOrderByCheckinDateDesc(userId).isEmpty();

        // Kiểm tra xem có phải là lần khôi phục không
        boolean isRecovery = false;
        if (!isFirstCheckIn && !wasCheckedInYesterday(userId) && canRecover) {
            isRecovery = true;
        }

        return new AttendanceStatusResponse(
                checkedInToday,
                totalPoints,
                streak,
                remainingRecoveries,
                canRecover,
                isFirstCheckIn,
                isRecovery);
    }

    private int getRemainingRecoveries(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalDate fisrtDayOfMonth = today.withDayOfMonth(1);

        int usedRecoveries = checkHistoryRepository.countByAccountIdAndIsRecoveryTrueAndCheckinDateAfter(userId,
                fisrtDayOfMonth);
        return 3 - usedRecoveries;
    }

    private boolean canRecoverStreak(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        boolean checkedInYesterday = checkHistoryRepository.existsByAccountIdAndCheckinDate(userId, yesterday);

        if (checkedInYesterday) {
            return false;
        }

        int remainingRecoveries = getRemainingRecoveries(userId);
        if (remainingRecoveries <= 0) {
            return false;
        }

        LocalDate dayBeforeYesterday = today.minusDays(2);
        boolean checkedInDayBeforeYesterday = checkHistoryRepository.existsByAccountIdAndCheckinDate(userId,
                dayBeforeYesterday);

        return checkedInDayBeforeYesterday;
    }

    public AttendanceCheckinResponse checkIn(Integer userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        boolean checkedInToday = checkHistoryRepository.existsByAccountIdAndCheckinDate(userId, today);
        if (checkedInToday) {
            throw new AppException(ErrorCode.INVALID_REQUEST); // Đã điểm danh hôm nay
        }

        // Kiểm tra xem có phải lần đầu điểm danh không
        boolean isFirstCheckIn = checkHistoryRepository.findByAccountIdOrderByCheckinDateDesc(userId).isEmpty();

        int streak = calculateStreak(userId);
        boolean wasCheckedInYesterday = wasCheckedInYesterday(userId);
        boolean isRecovery = false;

        if (isFirstCheckIn) {
            // Lần đầu điểm danh
            streak = 1;
        } else if (!wasCheckedInYesterday) {
            // Không phải lần đầu và hôm qua không điểm danh
            if (canRecoverStreak(userId)) {
                isRecovery = true;
                streak = streak + 1; // Khôi phục streak
            } else {
                streak = 1; // Bắt đầu streak mới
            }
        } else {
            // Hôm qua đã điểm danh
            streak = streak + 1; // Tăng streak
        }

        // Tính điểm thưởng
        int pointsReceived = calculatePoints(streak);
        int totalPoints = (account.getBonusPoint() != null ? account.getBonusPoint() : 0) + pointsReceived;

        // Cập nhật điểm
        account.setBonusPoint(totalPoints);
        accountRepository.save(account);

        // Lưu lịch sử điểm danh
        CheckinHistory history = CheckinHistory.builder()
                .account(account)
                .bonusPoint(pointsReceived)
                .checkinDate(today)
                .isRecovery(isRecovery)
                .recoveryCount(isRecovery ? getRemainingRecoveries(userId) : null)
                .build();
        checkHistoryRepository.save(history);

        return new AttendanceCheckinResponse(true, pointsReceived, totalPoints, streak, isRecovery);
    }

    private int calculatePoints(int streak) {
        if (streak > 7) {
            return 20;
        } else if (streak > 3) {
            return 15;
        } else if (streak > 1) {
            return 12;
        }
        return 10;
    }

    private int calculateStreak(Integer userId) {
        List<CheckinHistory> history = checkHistoryRepository.findByAccountIdOrderByCheckinDateDesc(userId);
        if (history.isEmpty()) {
            return 0;
        }
    
        Set<LocalDate> checkinDates = history.stream()
                .map(CheckinHistory::getCheckinDate)
                .filter(date -> !date.isAfter(LocalDate.now()))
                .collect(Collectors.toSet());
    
        LocalDate today = LocalDate.now();
    
        // Kiểm tra nếu hôm nay đã điểm danh và là ngày khôi phục
        Optional<CheckinHistory> todayHistoryOpt = history.stream()
                .filter(h -> today.equals(h.getCheckinDate()))
                .findFirst();
    
        if (todayHistoryOpt.isPresent() && Boolean.TRUE.equals(todayHistoryOpt.get().getIsRecovery())) {
            // Đã khôi phục: tính streak từ hôm kia + hôm nay
            LocalDate dayBeforeYesterday = today.minusDays(2);
            int streak = 1; // bắt đầu từ hôm kia
            LocalDate current = dayBeforeYesterday.minusDays(1);
            while (checkinDates.contains(current)) {
                streak++;
                current = current.minusDays(1);
            }
            return streak + 1; // cộng thêm hôm nay
        } else if (!checkinDates.contains(today) && canRecoverStreak(userId)) {
            // Chưa khôi phục nhưng có thể khôi phục: tính streak từ hôm kia
            LocalDate dayBeforeYesterday = today.minusDays(2);
            int streak = 1; // bắt đầu từ hôm kia
            LocalDate current = dayBeforeYesterday.minusDays(1);
            while (checkinDates.contains(current)) {
                streak++;
                current = current.minusDays(1);
            }
            return streak;
        } else {
            // Logic cũ: tính streak liên tục từ hôm nay/hôm qua
            int streak = 0;
            LocalDate current = today;
            if (!checkinDates.contains(current)) {
                current = current.minusDays(1);
            }
            while (checkinDates.contains(current)) {
                streak++;
                current = current.minusDays(1);
            }
            return streak;
        }
    }

    private boolean wasCheckedInYesterday(Integer userId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return checkHistoryRepository.existsByAccountIdAndCheckinDate(userId, yesterday);
    }
}
