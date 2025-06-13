package ute.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ute.entity.Account;
import ute.entity.ReadingProgress;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.AccountRepository;
import ute.repository.ReadingProgressRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadingProgressService {
    private final ReadingProgressRepository readingProgressRepository;
    private final AccountRepository accountRepository;

    private static final int POINTS_PER_5_MINUTES = 5;
    private static final int MAX_POINTS_PER_DAY = 20;
    private static final int SECONDS_PER_5_MINUTES = 300;

    public ReadingProgress updateProgress(Integer userId, Integer secondsRead) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        LocalDate today = LocalDate.now();

        // Tìm bản ghi hiện tại
        Optional<ReadingProgress> optionalProgress = readingProgressRepository
                .findByAccountIdAndReadingDate(userId, today);

        ReadingProgress progress;
        if (optionalProgress.isPresent()) {
            progress = optionalProgress.get();
        } else {
            progress = new ReadingProgress();
            progress.setAccount(account);
            progress.setReadingDate(today);
            progress.setTotalSeconds(0);
            progress.setTotalPoints(0);
        }

        int oldTotalSeconds = progress.getTotalSeconds();
        int newTotalSeconds = oldTotalSeconds + secondsRead;
        progress.setTotalSeconds(newTotalSeconds);

        int new5MinuteBlocks = newTotalSeconds / SECONDS_PER_5_MINUTES;
        int old5MinuteBlocks = oldTotalSeconds / SECONDS_PER_5_MINUTES;
        int additional5MinuteBlocks = new5MinuteBlocks - old5MinuteBlocks;

        if (additional5MinuteBlocks > 0) {
            int newPoints = Math.min(
                    progress.getTotalPoints() + (additional5MinuteBlocks * POINTS_PER_5_MINUTES),
                    MAX_POINTS_PER_DAY);
            progress.setTotalPoints(newPoints);
            account.setBonusPoint(account.getBonusPoint() + 5);
        }

        readingProgressRepository.save(progress);
        accountRepository.save(account);
        return progress;
    }

    public ReadingProgress getProgress(Integer userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        LocalDate today = LocalDate.now();

        // Tìm record
        Optional<ReadingProgress> optional = readingProgressRepository.findByAccountIdAndReadingDate(userId, today);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            // Nếu chưa có thì tạo mới và lưu vào DB
            ReadingProgress newProgress = ReadingProgress.builder()
                    .account(account)
                    .readingDate(today)
                    .totalSeconds(0)
                    .totalPoints(0)
                    .build();
            readingProgressRepository.save(newProgress);
            return newProgress;
        }
    }
}