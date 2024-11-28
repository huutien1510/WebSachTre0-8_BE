package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.response.CommentResponse;
import ute.dto.response.RatingResponse;
import ute.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServices {
    CommentRepository commentRepository;
    public List<CommentResponse> getCommentByChapter(Integer chapterID){
        return commentRepository.getCommentByChapter(chapterID);
    }
}
