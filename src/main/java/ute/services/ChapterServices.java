package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ute.dto.response.BookDetailResponse;
import ute.dto.response.ChapterResponse;
import ute.entity.Book;
import ute.entity.Chapter;
import ute.repository.ChapterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChapterServices {
    ChapterRepository chapterRepository;

    public List<ChapterResponse> getChapterByBook(Integer bookID) {
        return chapterRepository.getChapterByBook(bookID);
    }
}
