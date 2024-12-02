package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ute.dto.request.ChapterAddRequest;
import ute.dto.response.ChapterResponse;
import ute.entity.Account;
import ute.entity.Chapter;
import ute.repository.BookRepository;
import ute.repository.ChapterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChapterServices {
    ChapterRepository chapterRepository;
    BookRepository bookRepository;

    public List<ChapterResponse> getChapterByBook(Integer bookID) {
        return chapterRepository.getChapterByBook(bookID);
    }

    public ChapterResponse getChapterByID(Integer chapterID) {
        Chapter chapter = chapterRepository.findById(chapterID)
                .orElseThrow(()-> new RuntimeException("Chapter not found"));
        return new ChapterResponse(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getPushlishDate(),
                chapter.getViewCount(),
                chapter.getChapterNumber(),
                chapter.getBook().getId()
        );
    }

    public Long getTotalView() {
        return chapterRepository.getTotalView();
    }

    public Chapter addChapter(ChapterAddRequest body){
        Chapter chapter = Chapter.builder()
                .title(body.getTitle())
                .pushlishDate(body.getPushlishDate())
                .viewCount(0)
                .chapterNumber(body.getChapterNumber())
                .book(bookRepository.findById(body.getBookID()).orElseThrow(()->new RuntimeException("Book not found")))
                .build();
        return chapterRepository.save(chapter);
    }

    public Integer upViewChapter(Integer chapterID) {
        return chapterRepository.upView(chapterID);
    }

    public Chapter updateChapter(Integer chapterID, String newTitle){
        Chapter chapter = chapterRepository.findById(chapterID)
                .orElseThrow(()->new RuntimeException("Chapter not found"));
        chapter.setTitle(newTitle);
        return chapterRepository.save(chapter);
    }

    @Transactional
    public void deleteChapter(Integer chapterID){
        Chapter chapter = chapterRepository.findById(chapterID)
                        .orElseThrow(()->new RuntimeException("Chapter not found"));

        for (Account account : chapter.getAccounts()) {
            account.getChaptersReadingHistory().remove(chapter);
        }
        chapterRepository.deleteById(chapterID);
    }
}
