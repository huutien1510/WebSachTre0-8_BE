package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.response.ChapterContentResponse;
import ute.repository.ChapterContentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChapterContentService {
    ChapterContentRepository chapterContentRepository;
    public List<ChapterContentResponse> getContentByChapter(Integer chapterID){
        return chapterContentRepository.getContentByChapter(chapterID);
    }
}
