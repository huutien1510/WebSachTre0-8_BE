package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.request.ChapterContentRequest;
import ute.dto.response.ChapterContentResponse;
import ute.entity.ChapterContent;
import ute.repository.ChapterContentRepository;
import ute.repository.ChapterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChapterContentService {
    ChapterContentRepository chapterContentRepository;
    ChapterRepository chapterRepository;
    public List<ChapterContentResponse> getContentByChapter(Integer chapterID){
        return chapterContentRepository.getContentByChapter(chapterID);
    }

    public ChapterContent addContent(ChapterContentRequest body){
        ChapterContent chapterContent = ChapterContent.builder()
                .content(body.getContent())
                .contentNumber(body.getContentNumber())
                .chapter(chapterRepository.findById(body.getChapterID()).orElseThrow(()->new RuntimeException("Chapter not found")))
                .build();
        return  chapterContentRepository.save(chapterContent);
    }

    public void deleteContentByID(Integer contentID){
        chapterContentRepository.deleteById(contentID);
    }
}
