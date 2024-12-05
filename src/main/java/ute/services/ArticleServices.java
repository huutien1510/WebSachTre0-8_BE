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
import ute.entity.Article;
import ute.entity.Book;
import ute.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ArticleServices {
    ArticleRepository articleRepository;

    public Page<Article> getAllArticles(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAllByOrderByDateDesc(pageable);
    }
}
