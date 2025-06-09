package ute.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ute.dto.response.ReadingSessionResponse;
import ute.dto.response.ReadingStatusResponse;
import ute.dto.response.ReadingSummaryResponse;
import ute.entity.Account;
import ute.entity.Chapter;
import ute.entity.ReadingSession;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.AccountRepository;
import ute.repository.ChapterRepository;
import ute.repository.ReadingSessionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReadingService {
    private final ReadingSessionRepository readingSessionRepository;
    private final AccountRepository accountRepository;
    private final ChapterRepository chapterRepository;
    
    // Các hằng số cấu hình
    private static final int POINTS_PER_5_MINUTES = 1;
    private static final int MAX_POINTS_PER_DAY = 20;
    private static final int WARNING_THRESHOLD_MINUTES = 180;
    private static final int INACTIVITY_THRESHOLD_MINUTES = 5;
    private static final int WARNING_COOLDOWN_MINUTES = 30;
    
    public ReadingSessionResponse startReading(Integer userId, Integer chapterId) {
        log.info("Starting reading session for user {} and chapter {}", userId, chapterId);
        
        // Kiểm tra xem có phiên đọc đang active không
        Optional<ReadingSession> activeSession = readingSessionRepository
            .findByAccountIdAndIsActiveTrue(userId);
            
        if (activeSession.isPresent()) {
            log.warn("User {} already has an active reading session", userId);
            throw new AppException(ErrorCode.READING_SESSION_ACTIVE);
        }
        
        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));
            
        ReadingSession session = ReadingSession.builder()
            .account(account)
            .chapter(chapter)
            .startTime(LocalDateTime.now())
            .lastActiveTime(LocalDateTime.now())
            .isActive(true)
            .readingDate(LocalDate.now())
            .warningShown(false)
            .build();
            
        readingSessionRepository.save(session);
        log.info("Created new reading session with ID {} for user {}", session.getId(), userId);
        
        return mapToResponse(session);
    }
    
    public ReadingStatusResponse updateReadingStatus(Integer userId, Integer chapterId) {
        log.info("Updating reading status for user {} and chapter {}", userId, chapterId);
        
        ReadingSession session = readingSessionRepository
            .findByAccountIdAndChapterIdAndIsActiveTrue(userId, chapterId)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));
            
        LocalDateTime now = LocalDateTime.now();
        session.setLastActiveTime(now);
        
        // Tính thời gian đọc thực tế
        Duration readingDuration = Duration.between(session.getStartTime(), now);
        int totalMinutes = (int) readingDuration.toMinutes();
        session.setTotalReadingMinutes(totalMinutes);
        
        // Kiểm tra điều kiện hiển thị cảnh báo
        boolean shouldShowWarning = shouldDisplayWarning(session);
        if (shouldShowWarning) {
            session.setWarningShown(true);
            session.setLastWarningTime(now);
            log.info("Warning shown for user {} after {} minutes of reading", userId, totalMinutes);
        }
        
        // Tính điểm nếu đủ điều kiện
        if (shouldCalculatePoints(session)) {
            int points = calculatePoints(session);
            session.setPointsEarned(points);
            updateUserPoints(userId, points);
            log.info("Awarded {} points to user {} for reading", points, userId);
        }
        
        readingSessionRepository.save(session);
        
        return ReadingStatusResponse.builder()
            .readingMinutes(totalMinutes)
            .shouldShowWarning(shouldShowWarning)
            .warningMessage(shouldShowWarning ? generateWarningMessage(totalMinutes) : null)
            .pointsEarned(session.getPointsEarned())
            .build();
    }
    
    public ReadingSummaryResponse endReading(Integer userId, Integer chapterId) {
        log.info("Ending reading session for user {} and chapter {}", userId, chapterId);
        
        ReadingSession session = readingSessionRepository
            .findByAccountIdAndChapterIdAndIsActiveTrue(userId, chapterId)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));
            
        session.setEndTime(LocalDateTime.now());
        session.setIsActive(false);
        
        // Tính điểm cuối cùng
        if (shouldCalculatePoints(session)) {
            int points = calculatePoints(session);
            session.setPointsEarned(points);
            updateUserPoints(userId, points);
            log.info("Final points awarded: {} to user {}", points, userId);
        }
        
        readingSessionRepository.save(session);
        
        return mapToSummaryResponse(session);
    }
    
    // Các phương thức private helper
    private boolean shouldDisplayWarning(ReadingSession session) {
        if (session.getWarningShown()) {
            Duration sinceLastWarning = Duration.between(
                session.getLastWarningTime(), 
                LocalDateTime.now()
            );
            if (sinceLastWarning.toMinutes() < WARNING_COOLDOWN_MINUTES) {
                return false;
            }
        }
        
        return session.getTotalReadingMinutes() >= WARNING_THRESHOLD_MINUTES;
    }
    
    private boolean shouldCalculatePoints(ReadingSession session) {
        Duration inactivityDuration = Duration.between(
            session.getLastActiveTime(), 
            LocalDateTime.now()
        );
        
        if (inactivityDuration.toMinutes() > INACTIVITY_THRESHOLD_MINUTES) {
            log.debug("No points awarded due to inactivity for user {}", session.getAccount().getId());
            return false;
        }
        
        int pointsToday = readingSessionRepository
            .sumPointsEarnedByAccountIdAndReadingDate(
                session.getAccount().getId(), 
                session.getReadingDate()
            );
            
        if (pointsToday >= MAX_POINTS_PER_DAY) {
            log.debug("No points awarded - daily limit reached for user {}", session.getAccount().getId());
            return false;
        }
        
        return true;
    }
    
    private int calculatePoints(ReadingSession session) {
        int readingMinutes = session.getTotalReadingMinutes();
        int points = (readingMinutes / 5) * POINTS_PER_5_MINUTES;
        
        int pointsToday = readingSessionRepository
            .sumPointsEarnedByAccountIdAndReadingDate(
                session.getAccount().getId(), 
                session.getReadingDate()
            );
            
        return Math.min(points, MAX_POINTS_PER_DAY - pointsToday);
    }
    
    private void updateUserPoints(Integer userId, Integer points) {
        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            
        account.setBonusPoint(account.getBonusPoint() + points);
        accountRepository.save(account);
        log.info("Updated points for user {}: +{} points", userId, points);
    }
    
    private String generateWarningMessage(int readingMinutes) {
        return String.format(
            "Bạn đã đọc liên tục %d phút. Hãy nghỉ ngơi một chút để bảo vệ sức khỏe!",
            readingMinutes
        );
    }
    
    private ReadingSessionResponse mapToResponse(ReadingSession session) {
        return ReadingSessionResponse.builder()
            .sessionId(session.getId())
            .chapterId(session.getChapter().getId())
            .bookId(session.getChapter().getBook().getId())
            .startTime(session.getStartTime())
            .totalReadingMinutes(session.getTotalReadingMinutes())
            .pointsEarned(session.getPointsEarned())
            .isActive(session.getIsActive())
            .build();
    }
    
    private ReadingSummaryResponse mapToSummaryResponse(ReadingSession session) {
        return ReadingSummaryResponse.builder()
            .totalReadingMinutes(session.getTotalReadingMinutes())
            .pointsEarned(session.getPointsEarned())
            .startTime(session.getStartTime())
            .endTime(session.getEndTime())
            .build();
    }
}
