package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ute.dto.request.ReadingHistoryRequest;
import ute.dto.response.ReadingHistoryResponse;
import ute.entity.Account;
import ute.entity.Chapter;
import ute.repository.AccountRepository;
import ute.repository.ChapterRepository;
import ute.repository.ReadingHistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReadingHistoryServices {
    ReadingHistoryRepository readingHistoryRepository;
    ChapterRepository chapterRepository;
    AccountRepository accountRepository;
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ReadingHistoryResponse> getReadingHistoryByAccount(Integer accountID){
        return readingHistoryRepository.getReadingHistoryByAccount(accountID);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Account addReadingHistory(ReadingHistoryRequest body){
        Optional<Account> optionalAccount = Optional.of(accountRepository.findById(body.getAccountID())
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = optionalAccount.get();

        Optional<Chapter> optionalChapter = Optional.of(chapterRepository.findById(body.getChapterID())
                .orElseThrow(() -> new RuntimeException("Chapter not found")));
        Chapter chapter = optionalChapter.get();


        boolean chapterFound = false;
        List<Chapter> updatedChapters = new ArrayList<>();

        for (Chapter chapter1 : account.getChaptersReadingHistory()) {
            // Kiểm tra nếu bookId trùng và chapterNumber nhỏ hơn thì thay chapter
            if (Objects.equals(chapter1.getBook().getId(), chapter.getBook().getId())) {
                chapterFound = true;  // Đánh dấu là đã tìm thấy chapter cần thay
                if (chapter1.getChapterNumber() < chapter.getChapterNumber())
                    updatedChapters.add(chapter);  // Thay chapter cũ bằng chapter mới
                else
                    updatedChapters.add(chapter1);
            }
                 else
                    updatedChapters.add(chapter1);  // Giữ nguyên chapter cũ
        }
        // Nếu không có chapter trùng bookId, thêm chapter mới vào danh sách
        if (!chapterFound) {
            updatedChapters.add(chapter);  // Thêm chapter mới vào danh sách
        }

        // Cập nhật danh sách chapters của account
        account.setChaptersReadingHistory(updatedChapters);
            return accountRepository.save(account);
    }
}