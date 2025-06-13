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

        ReadingProgress progress = readingProgressRepository
                .findByAccountIdAndReadingDate(userId, today)
                .orElse(ReadingProgress.builder()
                        .account(account)
                        .readingDate(today)
                        .totalSeconds(0)
                        .totalPoints(0)
                        .build());


    public ReadingProgress getProgress(Integer userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        LocalDate today = LocalDate.now();

        // Tìm record
        Optional<ReadingProgress> optional = readingProgressRepository.findByAccountIdAndReadingDate(userId, today);

        if (optional.isPresent()) {
            return optional.get();
        } else {
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