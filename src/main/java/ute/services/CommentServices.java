package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.request.CommentRequest;
import ute.dto.response.CommentResponse;
import ute.dto.response.RatingResponse;
import ute.entity.Account;
import ute.entity.Chapter;
import ute.entity.Comment;
import ute.repository.AccountRepository;
import ute.repository.ChapterRepository;
import ute.repository.CommentRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServices {
    CommentRepository commentRepository;
    AccountRepository accountRepository;
    ChapterRepository chapterRepository;
    public List<CommentResponse> getCommentByChapter(Integer chapterID){
        return commentRepository.getCommentByChapter(chapterID);
    }

    public Comment postComment(CommentRequest body){
        Optional<Account> optionalAccount = Optional.of(accountRepository.findById(body.getAccountID())
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = optionalAccount.get();

        Optional<Chapter> optionalChapter = Optional.of(chapterRepository.findById(body.getChapterID())
                .orElseThrow(() -> new RuntimeException("Chapter not found")));
        Chapter chapter = optionalChapter.get();

        Comment comment = new Comment(null, body.getContent(), body.getPostDate(),chapter,account);

        return commentRepository.save(comment);
    }
}
